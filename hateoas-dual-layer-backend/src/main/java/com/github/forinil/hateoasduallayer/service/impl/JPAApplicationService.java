package com.github.forinil.hateoasduallayer.service.impl;

import com.github.forinil.hateoasduallayer.entity.Application;
import com.github.forinil.hateoasduallayer.model.ApplicationData;
import com.github.forinil.hateoasduallayer.profile.JPAData;
import com.github.forinil.hateoasduallayer.repository.ApplicationRepository;
import com.github.forinil.hateoasduallayer.service.ApplicationService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@JPAData
public class JPAApplicationService implements ApplicationService {

    private ApplicationRepository applicationRepository;

    private Mapper mapper;

    @Autowired
    public JPAApplicationService(ApplicationRepository applicationRepository,
                                 Mapper mapper) {
        this.applicationRepository = applicationRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ApplicationData> getApplication(int id) {
        Optional<Application> application = applicationRepository.findById(id);

        if (application.isPresent()) {
            ApplicationData applicationData = mapper.map(application.get(), ApplicationData.class);
            return Optional.of(applicationData);
        }

        return Optional.empty();
    }

    @Override
    public List<ApplicationData> getAllApplications() {
        Iterable<Application> applications = applicationRepository.findAll();
        List<ApplicationData> applicationDataList = new ArrayList<>();

        applications.forEach(application -> applicationDataList.add(mapper.map(application, ApplicationData.class)));

        return applicationDataList;
    }
}
