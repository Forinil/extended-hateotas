package com.github.forinil.hateoasduallayer.entity;

import com.github.forinil.hateoasduallayer.model.ApplicationData;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.util.List;
import java.util.UUID;

@Region("SearchCache")
@ToString
public class SearchResult {

    @Id
    @Getter
    private final UUID id;

    @Getter
    private List<ApplicationData> applicationList;

    @PersistenceConstructor
    public SearchResult(UUID id, List<ApplicationData> applicationList) {
        this.id = id;
        this.applicationList = applicationList;
    }

}
