package com.github.forinil.hateoasduallayer.controller;

import com.github.forinil.hateoasduallayer.describer.UserControllerDescriber;
import com.github.forinil.hateoasduallayer.model.UserData;
import com.github.forinil.hateoasduallayer.service.UserService;
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

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {

    private UserService userService;

    public UserController(UserControllerDescriber controllerDescriber,
                          UserService userService) {
        super(controllerDescriber);
        this.userService = userService;
    }

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds user by ID",
        notes = "ID must be an integer")
    @ApiResponses({@ApiResponse(code = 200, message = "User found"),
            @ApiResponse(code = 404, message = "User not found")})
    public HttpEntity<UserData> getUserData(@PathVariable(value="id") int id) {
        logger.debug("Requested user for ID: {}", id);
        Optional<UserData> userData = userService.getUser(id);

        userData.ifPresent(user -> user.add(linkTo(methodOn(UserController.class).getUserData(id)).withSelfRel()));

        if (!userData.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        logger.debug("Returning user: {}", userData.get());
        return ResponseEntity.status(HttpStatus.OK).body(userData.get());
    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Lists all users")
    @ApiResponses({@ApiResponse(code = 200, message = "Users found")})
    public HttpEntity<Resources<UserData>> getAllUsers() {
        logger.debug("Requested all applications");
        List<UserData> userDataList = userService.getAllUsers();

        userDataList.forEach(userData -> userData.add(linkTo(methodOn(UserController.class).getUserData(userData.getUserID())).withSelfRel()));

        Resources<UserData> resources = new Resources<>(userDataList, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

        logger.debug("Returning: {}", userDataList);
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }
}
