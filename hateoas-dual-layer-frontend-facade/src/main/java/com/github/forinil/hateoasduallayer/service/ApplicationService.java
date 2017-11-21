package com.github.forinil.hateoasduallayer.service;

import com.github.forinil.hateoasduallayer.model.ApplicationData;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationService {
    Optional<ApplicationData> getApplicationForID(int id);

    Pair<UUID, List<ApplicationData>> getApplicationList(UUID sid);
}
