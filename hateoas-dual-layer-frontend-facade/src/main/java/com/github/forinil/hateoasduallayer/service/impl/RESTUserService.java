package com.github.forinil.hateoasduallayer.service.impl;

import com.github.forinil.hateoasduallayer.client.RestClient;
import com.github.forinil.hateoasduallayer.model.UserData;
import com.github.forinil.hateoasduallayer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RESTUserService implements UserService {

    private RestClient restClient;

    @Autowired
    public RESTUserService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Optional<UserData> getUserForUID(int uid) {
        logger.debug("Getting user data for UID: {}", uid);
        return restClient.getUserDetails(uid);
    }
}
