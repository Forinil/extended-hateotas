package com.github.forinil.hateoasduallayer.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

public class SearchResult extends ApplicationDataList {

    @NonNull
    @Getter
    @Setter
    private UUID sid;
}
