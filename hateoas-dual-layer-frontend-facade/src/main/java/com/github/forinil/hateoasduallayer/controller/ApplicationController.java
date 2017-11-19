package com.github.forinil.hateoasduallayer.controller;

import com.github.forinil.hateoasduallayer.describer.ApplicationControllerDescriber;
import com.github.forinil.hateoasduallayer.model.Action;
import com.github.forinil.hateoasduallayer.model.ApplicationData;
import com.github.forinil.hateoasduallayer.model.ExtendedApplicationData;
import com.github.forinil.hateoasduallayer.model.UserData;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Resources;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/application")
public class ApplicationController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Value("${webservices.url:-http://localhost:8080/}")
    private String baseUrl;

    private ResponseErrorHandler responseErrorHandler;

    @Autowired
    ApplicationController(ApplicationControllerDescriber describer, ResponseErrorHandler responseErrorHandler) {
        super(describer);
        this.responseErrorHandler = responseErrorHandler;
    }

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds application by ID",
            notes = "ID must be an integer")
    public HttpEntity<ExtendedApplicationData> getApplicationData(@PathVariable(value="id") int id, @RequestParam(value="uid") int uid) {
        logger.debug("Requested application for ID: {}", id);

        // Define REST services URLs
        String applicationDetailsUrl = baseUrl + "application/details/";
        String userDetailsUrl = baseUrl + "user/details/";

        // Create rest template
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(responseErrorHandler);

        // Create request entity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        // Get user details
        logger.debug("Sending request to {}{}", userDetailsUrl, uid);

        ResponseEntity<UserData> userServiceResponse = restTemplate.exchange(userDetailsUrl + "{uid}", HttpMethod.GET, entity, UserData.class, uid);

        logger.debug("Received response: {}", userServiceResponse);

        // If user was not found then given UID is not authorized for the request
        if (userServiceResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            logger.debug("User service returned {} - user not authorized", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserData userData = userServiceResponse.getBody();

        //right_name = "R_" + action_name
        List<Action> actions = userData.getUserRights().stream().map(right -> Action.valueOf(right.name().substring(2))).collect(Collectors.toList());

        // If actions list does not contain 'DETAILS' user has no right to display application details
        if (!actions.contains(Action.DETAILS)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Get application details
        logger.debug("Sending request to {}{}", applicationDetailsUrl, id);
        ResponseEntity<ApplicationData> applicationServiceResponse = restTemplate.exchange(applicationDetailsUrl + "{id}", HttpMethod.GET, entity, ApplicationData.class, id);
        logger.debug("Received response: {}", applicationServiceResponse);

        if (applicationServiceResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            logger.debug("Application service returned {} - application not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }

        ApplicationData application = applicationServiceResponse.getBody();

        ExtendedApplicationData applicationData = new ExtendedApplicationData(application);
        applicationData.setAllowedActions(actions);

        applicationData.add(linkTo(methodOn(ApplicationController.class).getApplicationData(id, uid)).withSelfRel());

        logger.debug("Returning application: {}", applicationData);
        return ResponseEntity.status(HttpStatus.OK).body(applicationData);
    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Lists all applications")
    public HttpEntity<Resources<ApplicationData>> getAllApplications() {
        logger.debug("Requested all applications");
        List<ApplicationData> applicationDataList = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            ApplicationData applicationData = new ApplicationData();

            applicationData.add(linkTo(methodOn(ApplicationController.class).getApplicationData(i, 0)).withSelfRel());

            applicationDataList.add(applicationData);
        }

        Resources<ApplicationData> resources = new Resources<>(applicationDataList, linkTo(methodOn(ApplicationController.class).getAllApplications()).withSelfRel());

        logger.debug("Returning: {}", applicationDataList);
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }
}
