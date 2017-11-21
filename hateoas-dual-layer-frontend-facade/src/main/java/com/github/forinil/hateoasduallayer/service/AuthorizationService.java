package com.github.forinil.hateoasduallayer.service;

import com.github.forinil.hateoasduallayer.model.Action;
import com.github.forinil.hateoasduallayer.model.Right;

import java.util.List;

public interface AuthorizationService {
    boolean doesUserHaveRight(int uid, Right right);

    List<Action> getAvailableActionsForUser(int uid);
}
