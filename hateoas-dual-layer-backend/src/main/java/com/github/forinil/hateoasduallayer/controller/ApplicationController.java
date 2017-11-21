package com.github.forinil.hateoasduallayer.controller;

import com.github.forinil.hateoasduallayer.describer.ApplicationControllerDescriber;
import com.github.forinil.hateoasduallayer.model.ApplicationData;
import com.github.forinil.hateoasduallayer.model.ApplicationDataList;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/application")
@Slf4j
public class ApplicationController extends BaseController {

    @Autowired
    public ApplicationController(ApplicationControllerDescriber controllerDescriber) {
        super(controllerDescriber);
    }

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds application by ID",
            notes = "ID must be an integer")
    public HttpEntity<ApplicationData> getApplicationData(@PathVariable(value="id") int id) {
        logger.debug("Requested application for ID: {}", id);
        ApplicationData applicationData = new ApplicationData();

        applicationData.setApplicationId(id);
        applicationData.setContent("application");
        applicationData.setCreationDate(Calendar.getInstance().getTime());
        applicationData.add(linkTo(methodOn(ApplicationController.class).getApplicationData(id)).withSelfRel());

        logger.debug("Returning application: {}", applicationData);
        return ResponseEntity.status(HttpStatus.OK).body(applicationData);
    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Lists all applications")
    public HttpEntity<ApplicationDataList> getAllApplications() {
        logger.debug("Requested all applications");
        List<ApplicationData> applicationDataList = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            ApplicationData applicationData = new ApplicationData();
            applicationData.setApplicationId(i);
            applicationData.setContent("application");
            applicationData.setCreationDate(Calendar.getInstance().getTime());
            applicationData.add(linkTo(methodOn(ApplicationController.class).getApplicationData(i)).withSelfRel());

            applicationDataList.add(applicationData);
        }

        ApplicationDataList resources = new ApplicationDataList();
        resources.setApplicationList(applicationDataList);
        resources.add(linkTo(methodOn(ApplicationController.class).getAllApplications()).withSelfRel());

        logger.debug("Returning: {}", applicationDataList);
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }
}
