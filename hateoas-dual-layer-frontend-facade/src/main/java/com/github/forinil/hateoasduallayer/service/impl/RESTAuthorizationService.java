package com.github.forinil.hateoasduallayer.service.impl;

import com.github.forinil.hateoasduallayer.model.Action;
import com.github.forinil.hateoasduallayer.model.Right;
import com.github.forinil.hateoasduallayer.model.UserData;
import com.github.forinil.hateoasduallayer.service.AuthorizationService;
import com.github.forinil.hateoasduallayer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RESTAuthorizationService implements AuthorizationService {

    private UserService userService;

    @Autowired
    public RESTAuthorizationService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean doesUserHaveRight(int uid, Right right) {
        logger.debug("Checking if user {} has right {}", uid, right);
        Optional<UserData> userData = userService.getUserForUID(uid);

        return userData.filter(
                user -> user.getUserRights()
                        .stream()
                        .filter(userRight -> userRight.equals(right))
                        .collect(Collectors.toList()).size() > 0).isPresent();

    }

    @Override
    public List<Action> getAvailableActionsForUser(int uid) {
        logger.debug("Computing available actions for user {}", uid);
        Optional<UserData> userData = userService.getUserForUID(uid);

        if (!userData.isPresent()) {
            return new ArrayList<>();
        }

        //right_name = "R_" + action_name
        return userData.get()
                .getUserRights()
                .stream()
                .map(right -> Action.valueOf(right.name().substring(2)))
                .collect(Collectors.toList());
    }
}
