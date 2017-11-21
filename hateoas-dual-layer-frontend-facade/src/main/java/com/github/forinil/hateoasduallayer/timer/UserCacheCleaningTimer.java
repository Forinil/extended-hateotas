package com.github.forinil.hateoasduallayer.timer;

import com.github.forinil.hateoasduallayer.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCacheCleaningTimer {
    private UserRepository userRepository;

    @Autowired
    public UserCacheCleaningTimer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 30000)
    public void run() {
        logger.debug("Cleaning user cache");
        userRepository.deleteAll();
        logger.debug("Cleaning completed");
    }
}
