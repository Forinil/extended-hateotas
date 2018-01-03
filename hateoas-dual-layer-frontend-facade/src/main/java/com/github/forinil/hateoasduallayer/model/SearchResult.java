package com.github.forinil.hateoasduallayer.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SearchResult extends ApplicationDataList {

    @NonNull
    private UUID sid;
}
