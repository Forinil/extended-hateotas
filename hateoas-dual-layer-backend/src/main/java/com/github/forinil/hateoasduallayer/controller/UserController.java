package com.github.forinil.hateoasduallayer.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import static org.springframework.http.MediaType.*;

import com.github.forinil.hateoasduallayer.model.Right;
import com.github.forinil.hateoasduallayer.model.UserData;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds user by ID",
        notes = "ID must be an integer")
    public HttpEntity<UserData> getUserData(@PathVariable(value="id") int id) {
        logger.debug("Requested user for ID: {}", id);
        UserData userData = new UserData();

        userData.setUserID(id);
        userData.setUserLogin("TESTUSER");
        userData.setUserRights(new ArrayList<>(2));
        userData.getUserRights().add(Right.R_DELETE);
        userData.getUserRights().add(Right.R_LIST);
        userData.getUserRights().add(Right.R_SAVE);
        userData.getUserRights().add(Right.R_SEND);
        userData.getUserRights().add(Right.R_SIGN);
        userData.add(linkTo(methodOn(UserController.class).getUserData(id)).withSelfRel());

        logger.debug("Returning user: {}", userData);
        return new ResponseEntity<>(userData, HttpStatus.OK);

    }
}
