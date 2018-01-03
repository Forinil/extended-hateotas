package com.github.forinil.hateoasduallayer.config;

import com.github.forinil.hateoasduallayer.profile.JPAData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
@JPAData
public class JPAConfig {

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
        Resource applications = new ClassPathResource("applications.json");
        Resource users = new ClassPathResource("users.json");

        Jackson2RepositoryPopulatorFactoryBean repositoryPopulatorFactory = new Jackson2RepositoryPopulatorFactoryBean();
        repositoryPopulatorFactory.setResources(new Resource[]{applications, users});

        return repositoryPopulatorFactory;
    }
}
