package com.github.forinil.hateoasduallayer.controller;

import com.github.forinil.hateoasduallayer.describer.ControllerDescriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class IndexController extends BaseController {

    @Autowired
    public IndexController(@Qualifier("indexControllerDescriber") ControllerDescriber controllerDescriber) {
        super(controllerDescriber);
    }
}
