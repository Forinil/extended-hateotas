package com.github.forinil.hateoasduallayer.service;

import com.github.forinil.hateoasduallayer.model.UserData;

import java.util.Optional;

public interface UserService {
    Optional<UserData> getUserForUID(int uid);
}
