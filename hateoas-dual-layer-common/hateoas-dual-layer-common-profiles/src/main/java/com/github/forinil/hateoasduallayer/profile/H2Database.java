package com.github.forinil.hateoasduallayer.profile;

import org.springframework.context.annotation.Profile;

@Profile({"h2"})
public @interface H2Database {
}
