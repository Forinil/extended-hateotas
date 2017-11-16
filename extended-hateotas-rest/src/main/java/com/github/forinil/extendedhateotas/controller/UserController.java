package com.github.forinil.extendedhateotas.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import static org.springframework.http.MediaType.*;

import com.github.forinil.extendedhateotas.model.UserData;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds user by ID",
        notes = "ID must be an integer",
        response = UserData.class)
    public HttpEntity<UserData> getUserData(@PathVariable(value="id") int id) {
        UserData userData = new UserData();

        userData.setUserID(id);
        userData.setUserLogin("TESTUSER");
        userData.setUserRights(new ArrayList<>(2));
        userData.getUserRights().add("RIGHT1");
        userData.getUserRights().add("RIGHT2");
        userData.add(linkTo(methodOn(UserController.class).getUserData(id)).withSelfRel());

        return new ResponseEntity<>(userData, HttpStatus.OK);

    }
}
