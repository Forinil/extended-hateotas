package com.github.forinil.hateoasduallayer.repository;

import com.github.forinil.hateoasduallayer.entity.Application;
import com.github.forinil.hateoasduallayer.profile.JPAData;
import org.springframework.data.repository.CrudRepository;

@JPAData
public interface ApplicationRepository extends CrudRepository<Application, Integer> {
}
