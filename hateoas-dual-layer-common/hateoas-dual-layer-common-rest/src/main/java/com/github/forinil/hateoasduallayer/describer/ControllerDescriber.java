package com.github.forinil.hateoasduallayer.describer;

import org.springframework.hateoas.Link;

import java.util.List;

public interface ControllerDescriber {
    List<Link> getHATEOASLinks();
}
