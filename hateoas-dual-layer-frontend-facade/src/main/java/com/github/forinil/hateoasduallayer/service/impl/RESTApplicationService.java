package com.github.forinil.hateoasduallayer.service.impl;

import com.github.forinil.hateoasduallayer.client.RestClient;
import com.github.forinil.hateoasduallayer.entity.SearchResult;
import com.github.forinil.hateoasduallayer.model.ApplicationData;
import com.github.forinil.hateoasduallayer.repository.SearchResultRepository;
import com.github.forinil.hateoasduallayer.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RESTApplicationService implements ApplicationService {

    private RestClient restClient;
    private SearchResultRepository searchResultRepository;

    @Autowired
    public RESTApplicationService(RestClient restClient,
                                  SearchResultRepository searchResultRepository) {
        this.restClient = restClient;
        this.searchResultRepository = searchResultRepository;
    }

    @Override
    public Optional<ApplicationData> getApplicationForID(int id) {
        logger.debug("Getting application for ID: {}", id);
        return restClient.getApplicationDetails(id);
    }

    @Override
    public Pair<UUID, List<ApplicationData>> getApplicationList(UUID sid) {
        logger.debug("Retrieving application list for SID: {}", sid);
        Optional<SearchResult> searchResult;
        List<ApplicationData> dataList;

        if (sid != null) {
            logger.debug("Trying to retrieve search result for sid {} from cache", sid);
            searchResult = searchResultRepository.findById(sid);
        } else {
            searchResult = Optional.empty();
            sid = UUID.randomUUID();
        }

        if (searchResult.isPresent()) {
            //Get list of applications from cache
            dataList = searchResult.get().getApplicationList();
        } else {
            //Get list of applications from REST service
            Optional<List<ApplicationData>> applicationServiceResponse = restClient.getApplicationList();

            if (!applicationServiceResponse.isPresent()) {
                logger.debug("Application service returned {} - application list not found", HttpStatus.NOT_FOUND);
                return Pair.of(UUID.randomUUID(), new ArrayList<>());
            }

            dataList = applicationServiceResponse.get();

            SearchResult newSearchResult = new SearchResult(sid, dataList);
            logger.debug("Saving search result {} to cache", newSearchResult);
            searchResultRepository.save(newSearchResult);
        }

        return Pair.of(sid, dataList);
    }
}
