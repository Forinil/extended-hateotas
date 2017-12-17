package com.github.forinil.hateoasduallayer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class ApplicationDataList extends ResourceSupport {

    @NonNull
    @JsonProperty(value = "list")
    private List<ApplicationData> applicationList;
}
