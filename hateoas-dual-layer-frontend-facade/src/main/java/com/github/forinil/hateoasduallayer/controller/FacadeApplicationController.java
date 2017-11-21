package com.github.forinil.hateoasduallayer.controller;

import com.github.forinil.hateoasduallayer.describer.ApplicationControllerDescriber;
import com.github.forinil.hateoasduallayer.model.*;
import com.github.forinil.hateoasduallayer.service.ApplicationService;
import com.github.forinil.hateoasduallayer.service.AuthenticationService;
import com.github.forinil.hateoasduallayer.service.AuthorizationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/application")
@Slf4j
public class FacadeApplicationController extends BaseController {

    private AuthorizationService authorizationService;
    private AuthenticationService authenticationService;
    private ApplicationService applicationService;

    @Autowired
    FacadeApplicationController(ApplicationControllerDescriber describer,
                                AuthorizationService authorizationService,
                                AuthenticationService authenticationService,
                                ApplicationService applicationService) {
        super(describer);
        this.authorizationService = authorizationService;
        this.authenticationService = authenticationService;
        this.applicationService = applicationService;
    }

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds application by ID",
            notes = "ID must be an integer")
    @ApiResponses({@ApiResponse(code = 200, message = "Application found"),
            @ApiResponse(code = 401, message = "User not found for given ID"),
            @ApiResponse(code = 403, message = "User does not have a right to view application details"),
            @ApiResponse(code = 404, message = "Application not found")})
    public HttpEntity<ExtendedApplicationData> getApplicationData(@PathVariable(value="id") int id, @RequestParam(value="uid") int uid) {
        logger.debug("Requested application for ID: {} and UID: {}", id, uid);

        // If user was not found then given UID is not authorized for the request
        if (!authenticationService.isUserAuthenticated(uid)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Check if user has right to see the list of applications
        if (!authorizationService.doesUserHaveRight(uid, Right.R_DETAILS)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Action> actions = authorizationService.getAvailableActionsForUser(uid);

        // Get application details
        Optional<ApplicationData> applicationServiceResponse = applicationService.getApplicationForID(id);
        if (!applicationServiceResponse.isPresent()) {
            logger.debug("Application service returned {} - application not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }

        ApplicationData application = applicationServiceResponse.get();

        ExtendedApplicationData applicationData = new ExtendedApplicationData(application);
        applicationData.setAllowedActions(actions);

        applicationData.add(linkTo(methodOn(FacadeApplicationController.class).getApplicationData(id, uid)).withSelfRel());

        logger.debug("Returning application: {}", applicationData);
        return ResponseEntity.status(HttpStatus.OK).body(applicationData);
    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Lists all applications")
    @ApiResponses({@ApiResponse(code = 200, message = "Application list found"),
            @ApiResponse(code = 401, message = "User not found for given ID"),
            @ApiResponse(code = 403, message = "User does not have a right to view application list"),
            @ApiResponse(code = 404, message = "Application not found")})
    public HttpEntity<ApplicationDataList> getApplicationsList(@RequestParam(value="uid") int uid,
                                                               @RequestParam(value="sid", required = false) UUID sid) {
        logger.debug("Requested all applications for UID: {} and: SID {}", uid, sid);

        // If user was not found then given UID is not authorized for the request
        if (!authenticationService.isUserAuthenticated(uid)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Check if user has right to see the list of applications
        if (!authorizationService.doesUserHaveRight(uid, Right.R_LIST)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Pair<UUID, List<ApplicationData>> pair = applicationService.getApplicationList(sid);

        sid = pair.getFirst();
        List<ApplicationData> dataList = pair.getSecond();

        if (dataList.size() == 0) {
            logger.debug("Application service returned {} - application list not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }

        List<ApplicationData> applicationDataList = new ArrayList<>(dataList.size());

        dataList.forEach(
                applicationData -> {
                    ApplicationData data = new ApplicationData(applicationData);
                    data.add(linkTo(methodOn(FacadeApplicationController.class).getApplicationData(data.getApplicationId(), uid)).withSelfRel());
                    applicationDataList.add(data);
                }
        );

        com.github.forinil.hateoasduallayer.model.SearchResult resources = new com.github.forinil.hateoasduallayer.model.SearchResult();
        resources.setApplicationList(applicationDataList);
        resources.add(linkTo(methodOn(FacadeApplicationController.class).getApplicationsList(uid, sid)).withSelfRel());
        resources.setSid(sid);

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
        response.getLinks().add(linkTo(methodOn(FacadeApplicationController.class).getApplicationsList()).withSelfRel());
        response.getLinks().add(linkTo(methodOn(FacadeApplicationController.class).getApplicationsList(0, null)).withRel("example_query"));

        logger.debug("Returning: {}", response);
        return ResponseEntity.ok(response);
    }
}
