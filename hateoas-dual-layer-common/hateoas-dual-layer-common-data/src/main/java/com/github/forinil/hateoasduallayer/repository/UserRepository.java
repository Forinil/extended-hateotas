package com.github.forinil.hateoasduallayer.repository;

import com.github.forinil.hateoasduallayer.entity.User;
import com.github.forinil.hateoasduallayer.profile.JPAData;
import org.springframework.data.repository.CrudRepository;

@JPAData
public interface UserRepository extends CrudRepository<User, Integer> {
}
