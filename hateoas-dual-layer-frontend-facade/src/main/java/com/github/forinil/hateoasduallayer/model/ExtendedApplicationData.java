package com.github.forinil.hateoasduallayer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
public class ExtendedApplicationData extends ApplicationData {

    public ExtendedApplicationData(ApplicationData applicationData) {
        setApplicationId(applicationData.getApplicationId());
        setContent(applicationData.getContent());
        setCreationDate(applicationData.getCreationDate());
        setAllowedActions(new ArrayList<>());
    }

    @NonNull
    @Getter
    @Setter
    @JsonProperty
    private List<Action> allowedActions;
}
