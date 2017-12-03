package com.github.forinil.hateoasduallayer.controller;

import com.github.forinil.hateoasduallayer.describer.ApplicationControllerDescriber;
import com.github.forinil.hateoasduallayer.model.ApplicationData;
import com.github.forinil.hateoasduallayer.model.ApplicationDataList;
import com.github.forinil.hateoasduallayer.service.ApplicationService;
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
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/application")
@Slf4j
public class ApplicationController extends BaseController {

    private ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationControllerDescriber controllerDescriber,
                                 ApplicationService applicationService) {
        super(controllerDescriber);
        this.applicationService = applicationService;
    }

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds application by ID",
            notes = "ID must be an integer")
    public HttpEntity<ApplicationData> getApplicationData(@PathVariable(value="id") int id) {
        logger.debug("Requested application for ID: {}", id);
        Optional<ApplicationData> optionalApplicationData = applicationService.getApplication(id);

        if (optionalApplicationData.isPresent()) {
            ApplicationData applicationData = optionalApplicationData.get();
            applicationData.add(linkTo(methodOn(ApplicationController.class).getApplicationData(id)).withSelfRel());

            logger.debug("Returning application: {}", applicationData);
            return ResponseEntity.status(HttpStatus.OK).body(applicationData);
        } else {
            logger.debug("No application found for id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Lists all applications")
    public HttpEntity<ApplicationDataList> getAllApplications() {
        logger.debug("Requested all applications");
        List<ApplicationData> applicationDataList = applicationService.getAllApplications();

        applicationDataList.forEach(applicationData -> applicationData.add(
                linkTo(methodOn(ApplicationController.class)
                    .getApplicationData(applicationData.getApplicationId()))
                .withSelfRel()));

        ApplicationDataList resources = new ApplicationDataList();
        resources.setApplicationList(applicationDataList);
        resources.add(linkTo(methodOn(ApplicationController.class).getAllApplications()).withSelfRel());

        logger.debug("Returning: {}", applicationDataList);
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }
}
