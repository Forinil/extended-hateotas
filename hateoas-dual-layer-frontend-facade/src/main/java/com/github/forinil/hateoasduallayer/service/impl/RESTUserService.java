package com.github.forinil.hateoasduallayer.service.impl;

import com.github.forinil.hateoasduallayer.client.RestClient;
import com.github.forinil.hateoasduallayer.entity.User;
import com.github.forinil.hateoasduallayer.model.UserData;
import com.github.forinil.hateoasduallayer.repository.UserRepository;
import com.github.forinil.hateoasduallayer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RESTUserService implements UserService {

    private RestClient restClient;
    private UserRepository userRepository;

    @Autowired
    public RESTUserService(RestClient restClient,
                           UserRepository userRepository) {
        this.restClient = restClient;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserData> getUserForUID(int uid) {
        logger.debug("Getting user data for UID: {}", uid);

        Optional<UserData> userData;

        logger.debug("Trying to fetch user from cache");
        Optional<User> user = userRepository.findById(uid);

        boolean fromCache = user.isPresent();

        userData = user.map(user1 -> Optional.of(new UserData(user1.getUserID(),
                user1.getUserLogin(),
                user1.getUserRights())))
                .orElseGet(() -> restClient.getUserDetails(uid));

        if (!fromCache) {
            userData.ifPresent(userData1 -> userRepository.save(new User(userData1)));
        }

        return userData;
    }
}
