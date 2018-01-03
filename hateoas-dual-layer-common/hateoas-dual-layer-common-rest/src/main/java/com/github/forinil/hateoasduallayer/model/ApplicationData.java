package com.github.forinil.hateoasduallayer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class ApplicationData extends ResourceSupport {

    public ApplicationData(ApplicationData applicationData) {
        setApplicationId(applicationData.getApplicationId());
        setCreationDate(applicationData.getCreationDate());
        setContent(applicationData.getContent());
    }

    @JsonProperty("id")
    private int applicationId;

    @NonNull
    @JsonProperty
    private Date creationDate;

    @NonNull
    @JsonProperty
    private String content;
}
