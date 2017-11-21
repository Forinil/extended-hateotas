package com.github.forinil.hateoasduallayer.service.impl;

import com.github.forinil.hateoasduallayer.service.AuthenticationService;
import com.github.forinil.hateoasduallayer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RESTAuthenticationService implements AuthenticationService {

    private UserService userService;

    @Autowired
    public RESTAuthenticationService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isUserAuthenticated(int uid) {
        logger.debug("Checking if user {} is authenticated", uid);
        return userService.getUserForUID(uid).isPresent();
    }
}
