package com.github.forinil.hateoasduallayer.service.impl;

import com.github.forinil.hateoasduallayer.model.Right;
import com.github.forinil.hateoasduallayer.model.UserData;
import com.github.forinil.hateoasduallayer.profile.MockData;
import com.github.forinil.hateoasduallayer.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@MockData
@Service
public class MockUserService implements UserService {

    @Override
    public Optional<UserData> getUser(int id) {
        UserData userData = new UserData();

        if (id > 1) {
            return Optional.empty();
        }

        userData.setUserID(id);
        userData.setUserLogin(String.format("TESTUSER_%d", id));
        setRights(userData);

        return Optional.of(userData);
    }

    @Override
    public List<UserData> getAllUsers() {
        List<UserData> userDataList = new ArrayList<>(2);

        for (int i = 0; i < 2; i++) {
            UserData userData = new UserData();

            userData.setUserID(i);
            userData.setUserLogin(String.format("TESTUSER_%d", i));
            setRights(userData);

            userDataList.add(userData);
        }

        return userDataList;
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
