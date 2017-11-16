package com.github.forinil.extendedhateotas.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import static org.springframework.http.MediaType.*;

import com.github.forinil.extendedhateotas.model.ApplicationData;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.Resources;
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

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds application by ID",
            notes = "ID must be an integer",
            response = ApplicationData.class)
    public HttpEntity<ApplicationData> getApplicationData(@PathVariable(value="id") int id) {
        ApplicationData applicationData = new ApplicationData();

        applicationData.setApplicationId(id);
        applicationData.setContent("application");
        applicationData.setCreationDate(Calendar.getInstance().getTime());
        applicationData.add(linkTo(methodOn(ApplicationController.class).getApplicationData(id)).withSelfRel());

        return new ResponseEntity<>(applicationData, HttpStatus.OK);
    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Lists all applications",
            response = ApplicationData.class,
            responseContainer = "List")
    public HttpEntity<Resources<ApplicationData>> getAllApplications() {
        List<ApplicationData> applicationDataList = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            ApplicationData applicationData = new ApplicationData();
            applicationData.setApplicationId(i);
            applicationData.setContent("application");
            applicationData.setCreationDate(Calendar.getInstance().getTime());
            applicationData.add(linkTo(methodOn(ApplicationController.class).getApplicationData(i)).withSelfRel());

            applicationDataList.add(applicationData);
        }

        Resources<ApplicationData> resources = new Resources<>(applicationDataList, linkTo(methodOn(ApplicationController.class).getAllApplications()).withSelfRel());

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
