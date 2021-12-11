package com.hashedin.virtualproperty.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SwaggerRedirectController {

    @GetMapping("/")
    public ModelAndView redirectToSwaggerPage() {
        return new ModelAndView("redirect:/swagger-ui.html");
    }

    @GetMapping("/swagger")
    public ModelAndView redirectToSwaggerPageAgain() {
        return new ModelAndView("redirect:/swagger-ui.html");
    }
}
