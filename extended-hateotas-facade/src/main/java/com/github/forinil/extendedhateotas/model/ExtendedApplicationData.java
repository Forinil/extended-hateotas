package com.github.forinil.extendedhateotas.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@ToString
public class ExtendedApplicationData extends ApplicationData {

    @NonNull
    @Getter
    @Setter
    @JsonProperty
    private List<Action> allowedActions;
}
