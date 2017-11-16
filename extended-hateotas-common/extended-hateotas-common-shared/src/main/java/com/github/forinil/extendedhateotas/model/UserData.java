package com.github.forinil.extendedhateotas.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@NoArgsConstructor
@ToString
public class UserData extends ResourceSupport {

    @Getter
    @Setter
    @JsonProperty("id")
    private int userID;

    @NonNull
    @Getter
    @Setter
    @JsonProperty
    private String userLogin;

    @NonNull
    @Getter
    @Setter
    @JsonProperty
    private List<String> userRights;
}
