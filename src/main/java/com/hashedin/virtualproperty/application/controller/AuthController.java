package com.hashedin.virtualproperty.application.controller;

import com.hashedin.virtualproperty.application.request.LoginRequest;
import com.hashedin.virtualproperty.application.request.PasswordResetRequest;
import com.hashedin.virtualproperty.application.request.SignupRequest;
import com.hashedin.virtualproperty.application.response.AuthResponse;
import com.hashedin.virtualproperty.application.service.AuthService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public AuthResponse loginUser(@RequestBody LoginRequest request) throws MessagingException, TemplateException, IOException {
        return this.authService.loginUser(request.email, request.password);
    }

    @PostMapping("/signup")
    public AuthResponse signupUser(@RequestBody SignupRequest request) throws MessagingException, TemplateException, IOException {
        return this.authService.signupUser(request.email, request.password, request.name, request.mobile, request.address);
    }

    @GetMapping("/verify/{token}")
    public String verifyEmail(@PathVariable String token) {
        return this.authService.verifyEmailToken(token);
    }

    @GetMapping("/reset/{email}")
    public String generatePasswordResetEmail(@PathVariable String email) throws MessagingException, TemplateException, IOException {
        this.authService.sendPasswordResetEmail(email);
        return "Password link will be sent if an account with email " + email + " exists";
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestBody PasswordResetRequest body){
        this.authService.resetPassword(body.token, body.password);
        return "Password reset successful. Use login with new password";
    }
}
