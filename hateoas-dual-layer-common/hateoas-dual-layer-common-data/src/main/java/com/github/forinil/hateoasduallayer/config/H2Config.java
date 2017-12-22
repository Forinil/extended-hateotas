package com.github.forinil.hateoasduallayer.config;

import com.github.forinil.hateoasduallayer.config.properties.H2ConfigProperties;
import com.github.forinil.hateoasduallayer.profile.H2Database;
import lombok.extern.slf4j.Slf4j;
import org.h2.server.web.WebServer;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Configuration
@H2Database
@Slf4j
@EnableConfigurationProperties(H2ConfigProperties.class)
public class H2Config extends H2ConsoleAutoConfiguration {
    private static final String FIELD_NAME = "GENERIC";
    private static final String FIELD_MODIFIERS = "modifiers";

    @Value("${spring.datasource.driver-class-name}")
    private String connectionDriver;

    @Value("${spring.datasource.url}")
    private String connectionUrl;

    @Value("${spring.datasource.username}")
    private String connectionUserName;

    private H2ConfigProperties.Custom custom;

    public H2Config(H2ConfigProperties properties) {
        super(properties);
        this.custom = properties.getCustom();
        logger.debug("Checking if custom console is enabled: {}", custom.isEnabled());
    }

    @Bean
    @Override
    @ConditionalOnProperty(prefix = "spring.h2.console.custom", name = "enabled", havingValue = "true")
    public ServletRegistrationBean<WebServlet> h2Console(){
        String[] connectionList = new String[]{String.format("In-memory test DB|%s|%s|%s", connectionDriver, connectionUrl, connectionUserName)};

        try {
            Field genericField = WebServer.class.getDeclaredField(FIELD_NAME);
            genericField.setAccessible(true);

            Field modifiers = Field.class.getDeclaredField(FIELD_MODIFIERS);
            modifiers.setAccessible(true);
            modifiers.setInt(genericField, genericField.getModifiers() & ~Modifier.FINAL);

            genericField.set(null, connectionList);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Error setting JDBC connection URL in Console Servlet", e);
        }

        return super.h2Console();
    }
}
