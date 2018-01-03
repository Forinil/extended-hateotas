package com.github.forinil.hateoasduallayer.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.h2.console")
public class H2ConfigProperties extends H2ConsoleProperties {

    @Getter
    private final Custom custom = new Custom();

    @Getter
    @Setter
    public static class Custom {
        /**
         * Enable the custom console.
         */
        private boolean enabled = false;
    }
}
