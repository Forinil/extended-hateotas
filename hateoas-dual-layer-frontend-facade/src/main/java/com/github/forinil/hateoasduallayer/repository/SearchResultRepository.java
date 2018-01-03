package com.github.forinil.hateoasduallayer.repository;

import com.github.forinil.hateoasduallayer.entity.SearchResult;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SearchResultRepository extends CrudRepository<SearchResult, UUID> {
    Optional<SearchResult> findByCreationTime(Instant creationTime);

    List<SearchResult> findAllByCreationTimeBefore(Instant deadline);

    List<SearchResult> findAllByCreationTimeIsLessThanEqual(Instant deadline);
}
