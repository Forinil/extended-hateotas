package com.github.forinil.hateoasduallayer.service.impl;

import com.github.forinil.hateoasduallayer.entity.User;
import com.github.forinil.hateoasduallayer.model.UserData;
import com.github.forinil.hateoasduallayer.profile.JPAData;
import com.github.forinil.hateoasduallayer.repository.UserRepository;
import com.github.forinil.hateoasduallayer.service.UserService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@JPAData
public class JPAUserService implements UserService {

    private UserRepository userRepository;

    private Mapper mapper;

    @Autowired
    public JPAUserService(UserRepository userRepository,
                          Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<UserData> getUser(int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            UserData userData = mapper.map(user.get(), UserData.class);
            return Optional.of(userData);
        }

        return Optional.empty();
    }

    @Override
    public List<UserData> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserData> userDataList = new ArrayList<>();

        users.forEach(user -> userDataList.add(mapper.map(user, UserData.class)));

        return userDataList;
    }
}
