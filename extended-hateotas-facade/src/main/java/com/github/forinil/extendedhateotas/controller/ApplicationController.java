package com.github.forinil.extendedhateotas.controller;

import com.github.forinil.extendedhateotas.model.Action;
import com.github.forinil.extendedhateotas.model.ApplicationData;
import com.github.forinil.extendedhateotas.model.ExtendedApplicationData;
import com.github.forinil.extendedhateotas.model.UserData;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    private static Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Value("${webservices.url:-http://localhost:8080/}")
    private String baseUrl;

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds application by ID",
            notes = "ID must be an integer")
    public HttpEntity<ExtendedApplicationData> getApplicationData(@PathVariable(value="id") int id, @RequestParam(value="uid") int uid) {
        logger.debug("Requested application for ID: {}", id);
        RestTemplate restTemplate = new RestTemplate();

        String applicationDetailsUrl = baseUrl + "application/details/";
        String userDetailsUrl = baseUrl + "user/details/";

        // Get application details
        logger.debug("Sending request to {}{}", applicationDetailsUrl, id);
        ApplicationData application = restTemplate.getForObject(applicationDetailsUrl + "{id}", ApplicationData.class, id);
        logger.debug("Received response: {}", application);

        // Get user details
        logger.debug("Sending request to {}{}", userDetailsUrl, uid);
        UserData userData = restTemplate.getForObject(userDetailsUrl + "{id}", UserData.class, id);
        logger.debug("Received response: {}", userData);

        //right_name = "R_" + action_name
        List<Action> actions = userData.getUserRights().stream().map(right -> Action.valueOf(right.name().substring(2))).collect(Collectors.toList());

        ExtendedApplicationData applicationData = new ExtendedApplicationData(application);
        applicationData.setAllowedActions(actions);

        applicationData.add(linkTo(methodOn(ApplicationController.class).getApplicationData(id, uid)).withSelfRel());

        logger.debug("Returning application: {}", applicationData);
        return new ResponseEntity<>(applicationData, HttpStatus.OK);
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
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
