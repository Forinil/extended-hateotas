package com.github.forinil.hateoasduallayer.describer;

import com.github.forinil.hateoasduallayer.controller.ApplicationController;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ApplicationControllerDescriber implements ControllerDescriber {
    @Override
    public List<Link> getHATEOASLinks() {
        List<Link> links = new ArrayList<>(2);

        links.add(linkTo(methodOn(ApplicationController.class).index()).withSelfRel());
        links.add(linkTo(methodOn(ApplicationController.class).getApplicationsList()).withRel("list"));

        return links;
    }
}
