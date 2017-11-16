package com.github.forinil.extendedhateotas.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@NoArgsConstructor
@ToString
public class ApplicationData extends ResourceSupport {

    @Getter
    @Setter
    @JsonProperty("id")
    private int applicationId;

    @NonNull
    @Getter
    @Setter
    @JsonProperty
    private Date creationDate;

    @NonNull
    @Getter
    @Setter
    @JsonProperty
    private String content;
}
