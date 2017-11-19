package com.github.forinil.hateoasduallayer.client;

import com.github.forinil.hateoasduallayer.model.ApplicationData;
import com.github.forinil.hateoasduallayer.model.ApplicationDataList;
import com.github.forinil.hateoasduallayer.model.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class RestClient {
    private static Logger logger = LoggerFactory.getLogger(RestClient.class);

    @Value("${webservices.url:-http://localhost:8080/}")
    private String baseUrl;

    private String applicationDetailsUrl;
    private String applicationListUrl;
    private String userDetailsUrl;

    private RestTemplate restTemplate;
    private HttpEntity<Object> requestEntity;

    @Autowired
    public RestClient(ResponseErrorHandler responseErrorHandler) {
        // Create rest template
        this.restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(responseErrorHandler);

        // Create request entity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        requestEntity = new HttpEntity<>(headers);
    }

    @PostConstruct
    public void init() {
        // Define REST services URLs
        this.applicationDetailsUrl = baseUrl + "application/details/";
        this.applicationListUrl = baseUrl + "application/list";
        this.userDetailsUrl = baseUrl + "user/details/";
    }

    public Optional<UserData> getUserDetails(int uid) {
        logger.debug("Sending request to {}{}", userDetailsUrl, uid);
        ResponseEntity<UserData> userServiceResponse = restTemplate.exchange(userDetailsUrl + "{uid}", HttpMethod.GET, requestEntity, UserData.class, uid);
        logger.debug("Received response: {}", userServiceResponse);

        // If user was not found then given UID is not authorized for the request
        if (userServiceResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            logger.debug("User service returned {} - user not authorized", HttpStatus.NOT_FOUND);
            return Optional.empty();
        }

        return Optional.of(userServiceResponse.getBody());
    }

    public Optional<ApplicationData> getApplicationDetails(int id) {
        logger.debug("Sending request to {}{}", applicationDetailsUrl, id);
        ResponseEntity<ApplicationData> applicationServiceResponse = restTemplate.exchange(applicationDetailsUrl + "{id}", HttpMethod.GET, requestEntity, ApplicationData.class, id);
        logger.debug("Received response: {}", applicationServiceResponse);

        if (applicationServiceResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            logger.debug("Application service returned {} - application not found", HttpStatus.NOT_FOUND);
            return Optional.empty();
        }

        return Optional.of(applicationServiceResponse.getBody());
    }

    public Optional<List<ApplicationData>> getApplicationList() {
        logger.debug("Sending request to {}", applicationListUrl);
        ResponseEntity<ApplicationDataList> applicationServiceResponse = restTemplate.exchange(applicationListUrl, HttpMethod.GET, requestEntity, ApplicationDataList.class);
        logger.debug("Received response: {}", applicationServiceResponse);

        if (applicationServiceResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            logger.debug("Application service returned {} - application list not found", HttpStatus.NOT_FOUND);
            return Optional.empty();
        }

        List<ApplicationData> applicationList = applicationServiceResponse.getBody().getApplicationList();

        return Optional.of(applicationList);
    }
}
