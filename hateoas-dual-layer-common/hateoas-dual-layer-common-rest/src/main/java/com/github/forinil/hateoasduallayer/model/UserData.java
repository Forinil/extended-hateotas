package com.github.forinil.hateoasduallayer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserData extends ResourceSupport {

    @JsonProperty("id")
    private int userID;

    @NonNull
    @JsonProperty
    private String userLogin;

    @NonNull
    @JsonProperty
    private List<Right> userRights;
}
