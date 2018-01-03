package com.github.forinil.hateoasduallayer.entity;

import com.github.forinil.hateoasduallayer.model.ApplicationData;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Region("SearchCache")
@ToString
//@AllArgsConstructor(onConstructor_={@PersistenceConstructor})
public class SearchResult {

    @Id
    @Getter
    private final UUID id;

    @Getter
    private Instant creationTime;

    @Getter
    private List<ApplicationData> applicationList;

    @PersistenceConstructor
    public SearchResult(UUID id, Instant creationTime, List<ApplicationData> applicationList) {
        this.id = id;
        this.creationTime = creationTime;
        this.applicationList = applicationList;
    }

    public SearchResult(UUID id, List<ApplicationData> applicationList) {
        this.id = id;
        this.creationTime = Instant.now();
        this.applicationList = applicationList;
    }
}
