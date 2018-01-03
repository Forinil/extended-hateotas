package com.github.forinil.hateoasduallayer.service;

import com.github.forinil.hateoasduallayer.model.ApplicationData;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    Optional<ApplicationData> getApplication(int id);
    List<ApplicationData> getAllApplications();
}
