package com.github.forinil.hateoasduallayer.repository;

import com.github.forinil.hateoasduallayer.entity.SearchResult;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SearchResultRepository extends CrudRepository<SearchResult, UUID> {
}
