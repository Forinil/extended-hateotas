package com.github.forinil.hateoasduallayer.entity;

import com.github.forinil.hateoasduallayer.model.Right;
import com.github.forinil.hateoasduallayer.model.UserData;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.util.ArrayList;
import java.util.List;

@Region("Users")
@ToString
public class User {

    @Id
    @NonNull
    @Getter
    private Integer userID;

    @NonNull
    @Getter
    private String userLogin;

    @NonNull
    @Getter
    private List<Right> userRights;

    @PersistenceConstructor
    public User(int userID, String userLogin, List<Right> userRights) {
        this.userID = userID;
        this.userLogin = userLogin;
        this.userRights = new ArrayList<>(userRights.size());
        this.userRights.addAll(userRights);
    }

    public User(UserData userData) {
        this.userID = userData.getUserID();
        this.userLogin = userData.getUserLogin();
        this.userRights = new ArrayList<>(userData.getUserRights().size());
        this.userRights.addAll(userData.getUserRights());
    }
}
