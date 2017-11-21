package com.github.forinil.hateoasduallayer.controller;

import com.github.forinil.hateoasduallayer.client.RestClient;
import com.github.forinil.hateoasduallayer.describer.ApplicationControllerDescriber;
import com.github.forinil.hateoasduallayer.entity.SearchResult;
import com.github.forinil.hateoasduallayer.model.*;
import com.github.forinil.hateoasduallayer.repository.SearchResultRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/application")
@Slf4j
public class FacadeApplicationController extends BaseController {

    private RestClient restClient;

    private SearchResultRepository searchResultRepository;

    @Autowired
    FacadeApplicationController(ApplicationControllerDescriber describer,
                                RestClient restClient,
                                SearchResultRepository searchResultRepository) {
        super(describer);
        this.restClient = restClient;
        this.searchResultRepository = searchResultRepository;
    }

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Finds application by ID",
            notes = "ID must be an integer")
    @ApiResponses({@ApiResponse(code = 200, message = "Application found"),
            @ApiResponse(code = 401, message = "User not found for given ID"),
            @ApiResponse(code = 403, message = "User does not have a right to view application details"),
            @ApiResponse(code = 404, message = "Application not found")})
    public HttpEntity<ExtendedApplicationData> getApplicationData(@PathVariable(value="id") int id, @RequestParam(value="uid") int uid) {
        logger.debug("Requested application for ID: {}", id);

        // Get user details
        Optional<UserData> userServiceResponse = restClient.getUserDetails(uid);

        // If user was not found then given UID is not authorized for the request
        if (!userServiceResponse.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserData userData = userServiceResponse.get();

        //right_name = "R_" + action_name
        List<Action> actions = userData.getUserRights().stream().map(right -> Action.valueOf(right.name().substring(2))).collect(Collectors.toList());

        // If actions list does not contain 'DETAILS' user has no right to display application details
        if (!actions.contains(Action.DETAILS)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Get application details
        Optional<ApplicationData> applicationServiceResponse = restClient.getApplicationDetails(id);

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

        // Get user details
        Optional<UserData> userServiceResponse = restClient.getUserDetails(uid);

        // If user was not found then given UID is not authorized for the request
        if (!userServiceResponse.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserData userData = userServiceResponse.get();

        // Check if user has righ to see the list of applications
        if (!userData.getUserRights().contains(Right.R_LIST)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<SearchResult> searchResult;
        List<ApplicationData> dataList;

        if (sid != null) {
            logger.debug("Trying to retrieve search result for sid {} from cache", sid);
            searchResult = searchResultRepository.findById(sid);
        } else {
            searchResult = Optional.empty();
            sid = UUID.randomUUID();
        }

        if (searchResult.isPresent()) {
            //Get list of applications from cache
            dataList = searchResult.get().getApplicationList();
        } else {
            //Get list of applications from REST service
            Optional<List<ApplicationData>> applicationServiceResponse = restClient.getApplicationList();

            if (!applicationServiceResponse.isPresent()) {
                logger.debug("Application service returned {} - application list not found", HttpStatus.NOT_FOUND);
                return ResponseEntity.notFound().build();
            }

            dataList = applicationServiceResponse.get();

            SearchResult newSearchResult = new SearchResult(sid, dataList);
            logger.debug("Saving search result {} to cache", searchResult);
            searchResultRepository.save(newSearchResult);
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
