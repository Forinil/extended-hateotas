package com.github.forinil.hateoasduallayer.config;

import com.github.forinil.hateoasduallayer.profile.JPAData;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@JPAData
public class DozerConfig {

    @Bean
    public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean() {
        Resource applicationMapping = new ClassPathResource("dozer/application-mapping.xml");
        Resource userMapping = new ClassPathResource("dozer/user-mapping.xml");

        DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
        dozerBeanMapperFactoryBean.setMappingFiles(new Resource[]{applicationMapping, userMapping});

        return dozerBeanMapperFactoryBean;
    }
}
