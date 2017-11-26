package com.github.forinil.hateoasduallayer.service;

import com.github.forinil.hateoasduallayer.model.UserData;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserData> getUser(int id);
    List<UserData> getAllUsers();
}
