package com.github.forinil.hateoasduallayer.entity;

import com.github.forinil.hateoasduallayer.model.ApplicationData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Region;

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
    private LocalDate creationDate;

    @Getter
    private List<ApplicationData> applicationList;

    @PersistenceConstructor
    public SearchResult(UUID id, LocalDate creationDate, List<ApplicationData> applicationList) {
        this.id = id;
        this.creationDate = creationDate;
        this.applicationList = applicationList;
    }

    public SearchResult(UUID id, List<ApplicationData> applicationList) {
        this.id = id;
        this.creationDate = LocalDate.now();
        this.applicationList = applicationList;
    }
}
