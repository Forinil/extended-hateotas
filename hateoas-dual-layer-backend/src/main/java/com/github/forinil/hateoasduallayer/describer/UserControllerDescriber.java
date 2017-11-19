package com.github.forinil.hateoasduallayer.describer;

import com.github.forinil.hateoasduallayer.controller.UserController;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserControllerDescriber implements ControllerDescriber {
    @Override
    public List<Link> getHATEOASLinks() {
        List<Link> links = new ArrayList<>(1);

        links.add(linkTo(methodOn(UserController.class).index()).withSelfRel());
        links.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("list"));

        return links;
    }
}
