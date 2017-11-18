package com.github.forinil.hateoasduallayer.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import static org.springframework.http.MediaType.*;

import com.github.forinil.hateoasduallayer.describer.ControllerDescriber;
import com.github.forinil.hateoasduallayer.describer.UserControllerDescriber;
import com.github.forinil.hateoasduallayer.model.Right;
import com.github.forinil.hateoasduallayer.model.UserData;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserControllerDescriber controllerDescriber) {
        super(controllerDescriber);
    }

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds user by ID",
        notes = "ID must be an integer")
    public HttpEntity<UserData> getUserData(@PathVariable(value="id") int id) {
        logger.debug("Requested user for ID: {}", id);
        UserData userData = new UserData();

        userData.setUserID(id);
        userData.setUserLogin("TESTUSER");
        setRights(userData);
        userData.add(linkTo(methodOn(UserController.class).getUserData(id)).withSelfRel());

        logger.debug("Returning user: {}", userData);
        return new ResponseEntity<>(userData, HttpStatus.OK);

    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Lists all users")
    public HttpEntity<Resources<UserData>> getAllUsers() {
        logger.debug("Requested all applications");
        List<UserData> userDataList = new ArrayList<>(2);

        for (int i = 0; i < 2; i++) {
            UserData userData = new UserData();

            userData.setUserID(i);
            userData.setUserLogin(String.format("TESTUSER_%d", i));
            setRights(userData);
            userData.add(linkTo(methodOn(UserController.class).getUserData(i)).withSelfRel());

            userDataList.add(userData);
        }

        Resources<UserData> resources = new Resources<>(userDataList, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

        logger.debug("Returning: {}", userDataList);
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    private void setRights(UserData userData) {
        if (userData.getUserID() == 0) {
            userData.setUserRights(new ArrayList<>(5));
            userData.getUserRights().add(Right.R_DELETE);
            userData.getUserRights().add(Right.R_LIST);
            userData.getUserRights().add(Right.R_SAVE);
            userData.getUserRights().add(Right.R_SEND);
            userData.getUserRights().add(Right.R_SIGN);
        } else {
            userData.setUserRights(new ArrayList<>(1));
            userData.getUserRights().add(Right.R_LIST);
        }
    }
}
