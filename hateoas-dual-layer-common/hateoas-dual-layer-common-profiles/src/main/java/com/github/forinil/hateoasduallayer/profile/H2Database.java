package com.github.forinil.hateoasduallayer.profile;

import org.springframework.context.annotation.Profile;

@Profile({"jpa_data", "h2"})
public @interface H2Database {
}
