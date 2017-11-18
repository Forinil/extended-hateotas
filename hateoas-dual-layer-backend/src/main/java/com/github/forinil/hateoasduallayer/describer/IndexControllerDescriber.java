package com.github.forinil.hateoasduallayer.describer;

import com.github.forinil.hateoasduallayer.controller.ApplicationController;
import com.github.forinil.hateoasduallayer.controller.IndexController;
import com.github.forinil.hateoasduallayer.controller.UserController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
@Qualifier("indexControllerDescriber")
public class IndexControllerDescriber implements ControllerDescriber {

    @Override
    public List<Link> getHATEOASLinks() {
        List<Link> links = new ArrayList<>(3);

        links.add(linkTo(methodOn(IndexController.class).index()).withSelfRel());
        links.add(linkTo(methodOn(UserController.class).index()).withRel("users"));
        links.add(linkTo(methodOn(ApplicationController.class).index()).withRel("applications"));

        return links;
    }
}
