package com.github.forinil.hateoasduallayer.service.impl;

import com.github.forinil.hateoasduallayer.model.ApplicationData;
import com.github.forinil.hateoasduallayer.profile.MockData;
import com.github.forinil.hateoasduallayer.service.ApplicationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@MockData
@Service
public class MockApplicationService implements ApplicationService {

    @Override
    public Optional<ApplicationData> getApplication(int id) {
        if (id > 2) {
            return Optional.empty();
        }

        ApplicationData applicationData = new ApplicationData();

        applicationData.setApplicationId(id);
        applicationData.setContent(String.format("application %d", id));
        applicationData.setCreationDate(Calendar.getInstance().getTime());

        return Optional.of(applicationData);

    }

    @Override
    public List<ApplicationData> getAllApplications() {
        List<ApplicationData> applicationDataList = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            ApplicationData applicationData = new ApplicationData();
            applicationData.setApplicationId(i);
            applicationData.setContent(String.format("application %d", i));
            applicationData.setCreationDate(Calendar.getInstance().getTime());

            applicationDataList.add(applicationData);
        }

        return applicationDataList;
    }
}
