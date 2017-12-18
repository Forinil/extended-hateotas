package com.github.forinil.hateoasduallayer.config;

import com.github.forinil.hateoasduallayer.profile.H2Database;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@H2Database
public class H2Config {

    @Bean
    public ServletRegistrationBean<WebServlet> servletRegistrationBean(){
        return new ServletRegistrationBean<>(new WebServlet(),"/console/*");
    }
}
