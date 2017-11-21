package com.github.forinil.hateoasduallayer.repository;

import com.github.forinil.hateoasduallayer.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
