package com.github.forinil.hateoasduallayer.controller;

import com.github.forinil.hateoasduallayer.describer.UserControllerDescriber;
import com.github.forinil.hateoasduallayer.model.Right;
import com.github.forinil.hateoasduallayer.model.UserData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {

    public UserController(UserControllerDescriber controllerDescriber) {
        super(controllerDescriber);
    }

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds user by ID",
        notes = "ID must be an integer")
    @ApiResponses({@ApiResponse(code = 200, message = "User found"),
            @ApiResponse(code = 404, message = "User not found")})
    public HttpEntity<UserData> getUserData(@PathVariable(value="id") int id) {
        logger.debug("Requested user for ID: {}", id);
        UserData userData = new UserData();

        if (id > 1) {
            return ResponseEntity.notFound().build();
        }

        userData.setUserID(id);
        userData.setUserLogin(String.format("TESTUSER_%d", id));
        setRights(userData);
        userData.add(linkTo(methodOn(UserController.class).getUserData(id)).withSelfRel());

        logger.debug("Returning user: {}", userData);
        return ResponseEntity.status(HttpStatus.OK).body(userData);

    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Lists all users")
    @ApiResponses({@ApiResponse(code = 200, message = "Users found")})
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
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }

    private void setRights(UserData userData) {
        if (userData.getUserID() == 0) {
            userData.setUserRights(new ArrayList<>(5));
            userData.getUserRights().add(Right.R_DELETE);
            userData.getUserRights().add(Right.R_DETAILS);
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
