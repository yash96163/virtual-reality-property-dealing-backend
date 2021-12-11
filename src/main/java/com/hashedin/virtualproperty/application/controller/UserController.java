package com.hashedin.virtualproperty.application.controller;

import com.hashedin.virtualproperty.application.request.UserUpdateRequest;
import com.hashedin.virtualproperty.application.response.UserResponse;
import com.hashedin.virtualproperty.application.service.UserService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/{userEmail}")
    public UserResponse getUserByID(@PathVariable String userEmail, @RequestHeader(name = "Authorization") String token) throws MessagingException, TemplateException, IOException {
        return userService.getUserDetail(userEmail, token);
    }

    @PutMapping(value = "/user/")
    public UserResponse updateUserById(@RequestBody UserUpdateRequest user, @RequestHeader(name = "Authorization") String token) {
        return userService.updateUserById(user, token);
    }

    @PostMapping("/user/image")
    public UserResponse addImage(
            @RequestHeader(name = "Authorization", defaultValue = "") String token,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        return this.userService.addImage(image, token);
    }
}
