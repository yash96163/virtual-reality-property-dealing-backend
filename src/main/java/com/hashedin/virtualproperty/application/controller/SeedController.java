package com.hashedin.virtualproperty.application.controller;

import com.hashedin.virtualproperty.application.service.DbSeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeedController {
    @Autowired
    private DbSeedService seedService;

    @GetMapping("/seed")
    public String seed() {
        this.seedService.seedDatabase();
        return "OK";
    }
}
