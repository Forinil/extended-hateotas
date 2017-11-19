package com.github.forinil.hateoasduallayer.controller;

import com.github.forinil.hateoasduallayer.client.RestClient;
import com.github.forinil.hateoasduallayer.describer.ApplicationControllerDescriber;
import com.github.forinil.hateoasduallayer.model.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/application")
public class ApplicationController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    private RestClient restClient;

    @Autowired
    ApplicationController(ApplicationControllerDescriber describer, RestClient restClient) {
        super(describer);
        this.restClient = restClient;
    }

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds application by ID",
            notes = "ID must be an integer")
    @ApiResponses({@ApiResponse(code = 200, message = "Application found"),
            @ApiResponse(code = 401, message = "User not found for given ID"),
            @ApiResponse(code = 403, message = "User does not have a right to view application details"),
            @ApiResponse(code = 404, message = "Application not found")})
    public HttpEntity<ExtendedApplicationData> getApplicationData(@PathVariable(value="id") int id, @RequestParam(value="uid") int uid) {
        logger.debug("Requested application for ID: {}", id);

        // Get user details
        Optional<UserData> userServiceResponse = restClient.getUserDetails(uid);

        // If user was not found then given UID is not authorized for the request
        if (!userServiceResponse.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserData userData = userServiceResponse.get();

        //right_name = "R_" + action_name
        List<Action> actions = userData.getUserRights().stream().map(right -> Action.valueOf(right.name().substring(2))).collect(Collectors.toList());

        // If actions list does not contain 'DETAILS' user has no right to display application details
        if (!actions.contains(Action.DETAILS)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Get application details
        Optional<ApplicationData> applicationServiceResponse = restClient.getApplicationDetails(id);

        if (!applicationServiceResponse.isPresent()) {
            logger.debug("Application service returned {} - application not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }

        ApplicationData application = applicationServiceResponse.get();

        ExtendedApplicationData applicationData = new ExtendedApplicationData(application);
        applicationData.setAllowedActions(actions);

        applicationData.add(linkTo(methodOn(ApplicationController.class).getApplicationData(id, uid)).withSelfRel());

        logger.debug("Returning application: {}", applicationData);
        return ResponseEntity.status(HttpStatus.OK).body(applicationData);
    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Lists all applications")
    @ApiResponses({@ApiResponse(code = 200, message = "Application list found"),
            @ApiResponse(code = 401, message = "User not found for given ID"),
            @ApiResponse(code = 403, message = "User does not have a right to view application list"),
            @ApiResponse(code = 404, message = "Application not found")})
    public HttpEntity<ApplicationDataList> getApplicationsList(@RequestParam(value="uid") int uid) {
        logger.debug("Requested all applications");

        // Get user details
        Optional<UserData> userServiceResponse = restClient.getUserDetails(uid);

        // If user was not found then given UID is not authorized for the request
        if (!userServiceResponse.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserData userData = userServiceResponse.get();

        // Check if user has righ to see the list of applications
        if (!userData.getUserRights().contains(Right.R_LIST)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        //Get list of applications
        Optional<List<ApplicationData>> applicationServiceResponse = restClient.getApplicationList();

        if (!applicationServiceResponse.isPresent()) {
            logger.debug("Application service returned {} - application list not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }

        List<ApplicationData> applicationDataList = new ArrayList<>(applicationServiceResponse.get().size());

        applicationServiceResponse.get().forEach(
                applicationData -> {
                    ApplicationData data = new ApplicationData(applicationData);
                    data.add(linkTo(methodOn(ApplicationController.class).getApplicationData(data.getApplicationId(), uid)).withSelfRel());
                    applicationDataList.add(data);
                }
        );

        ApplicationDataList resources = new ApplicationDataList();
        resources.setApplicationList(applicationDataList);
        resources.add(linkTo(methodOn(ApplicationController.class).getApplicationsList(uid)).withSelfRel());

        logger.debug("Returning: {}", applicationDataList);
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }

    @GetMapping(value = "/list/info", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Returns an example of proper application list query")
    @ApiResponses({@ApiResponse(code = 200, message = "Response sent")})
    public HttpEntity<Resource<String>> getApplicationsList() {
        logger.debug("Requested all applications without providing UID");

        String message = "You must provide an UID to fetch an application list - see example link";

        Resource<String> response = new Resource<>(message);
        response.getLinks().add(linkTo(methodOn(ApplicationController.class).getApplicationsList()).withSelfRel());
        response.getLinks().add(linkTo(methodOn(ApplicationController.class).getApplicationsList(0)).withRel("example_query"));

        logger.debug("Returning: {}", response);
        return ResponseEntity.ok(response);
    }
}
