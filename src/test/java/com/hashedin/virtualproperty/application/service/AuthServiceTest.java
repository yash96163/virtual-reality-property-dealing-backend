package com.hashedin.virtualproperty.application.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.hashedin.virtualproperty.application.entities.User;
import com.hashedin.virtualproperty.application.exceptions.CustomException;
import com.hashedin.virtualproperty.application.exceptions.InvalidRequest;
import com.hashedin.virtualproperty.application.exceptions.UnauthorizedException;
import com.hashedin.virtualproperty.application.repository.UserRepository;
import com.hashedin.virtualproperty.application.response.AuthResponse;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.mail.MessagingException;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    @Mock private UserRepository userRepository;
    @InjectMocks private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void checkingCorrectUserLoginWithTokenValidation() throws MessagingException, TemplateException, IOException {
        String password = "123456789";
        String hashedPassword = BCrypt.withDefaults().hashToString(10, password.toCharArray());
        User user =
                new User("xyz@gmail.com", hashedPassword, "xyz", "9666314745", "hashedin university");
        user.setVerified(true);
        Optional<User> user1 = Optional.ofNullable(user);
        Mockito.when(userRepository.findById("xyz@gmail.com")).thenReturn(user1);

        AuthResponse authResponse = authService.loginUser("xyz@gmail.com", "123456789");
        assertEquals("xyz@gmail.com", authResponse.email);
        assertEquals("xyz", authResponse.name);
        assertEquals("9666314745", authResponse.mobile);
        assertEquals("hashedin university", authResponse.address);
        assertTrue(authResponse.token.length() > 0);
        assertEquals(authService.getUserFromToken(authResponse.token).getEmail(), authResponse.email);
    }

    @Test
    void test_shouldThrowCustomExceptionWhenLoginAndUserDoesNotExist() {
        Optional<User> mockDbResponse = Optional.empty();
        Mockito.when(userRepository.findById("xyz@gmail.com")).thenReturn(mockDbResponse);
        CustomException exception =
                assertThrows(
                        CustomException.class,
                        () -> {
                            authService.loginUser("xyz@gmail.com", "123456789");
                        },
                        "Exception not thrown");
        assertEquals(exception.getMessage(), "User with email xyz@gmail.com does not exist");
    }

    @Test
    void test_shouldInvalidRequestExceptionWhenEmailAndPasswordAreInvalid() {
        InvalidRequest exception =
                assertThrows(
                        InvalidRequest.class,
                        () -> {
                            authService.loginUser("xyz@gmail", "123");
                        },
                        "Exception not thrown");
        assertEquals(
                exception.getMessage(),
                "Email is invalid, Length of Password should be atleast 6 characters");
    }

    @Test
    void test_shouldCustomRequestWhenPasswordIsInvalid() {
        User user = new User("xyz@gmail.com", "randomHash", "xyz", "9666314745", "hashedin university");
        Optional<User> mockResponse = Optional.of(user);
        Mockito.when(userRepository.findById("xyz@gmail.com")).thenReturn(mockResponse);
        CustomException exception =
                assertThrows(
                        CustomException.class,
                        () -> {
                            authService.loginUser("xyz@gmail.com", "wrongpassword");
                        },
                        "Exception not thrown");
        assertEquals(exception.getMessage(), "Invalid login credentials provided");
    }

    @Test
    void test_successfulSignupOfUser() throws MessagingException, TemplateException, IOException {
        // returns the object it was passed
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(returnsFirstArg());
        AuthResponse response =
                authService.signupUser(
                        "xyz@test.com", "12345678", "Test User", "9876543210", "Test Address");
        assertEquals(response.email, "xyz@test.com");
        assertEquals(response.address, "Test Address");
        assertEquals(response.mobile, "9876543210");
        assertEquals(response.name, "Test User");
        // check token
        String decodedEmail = authService.getUserFromToken(response.token).getEmail();
        assertEquals(decodedEmail, response.email);
    }

    @Test
    void test_signUpFailDuplicateEmail() {
        Mockito.when(userRepository.countOfUsersByEmailOrMobile("xyz@test.com", "9876543210"))
                .thenReturn(1);
        CustomException ex =
                assertThrows(
                        CustomException.class,
                        () -> {
                            authService.signupUser(
                                    "xyz@test.com", "123456", "Test User", "9876543210", "Test Address");
                        },
                        "Custom exception was expected");
        assertEquals(ex.getMessage(), "Email or mobile is not unique");
    }

    @Test
    void test_signUpFailInvalidName() {
        InvalidRequest ex =
                assertThrows(
                        InvalidRequest.class,
                        () -> {
                            authService.signupUser("test@test.com", "123456", "T", "9876543210", "Test Address");
                        });
        assertEquals(
                ex.getMessage(),
                "Name should be atleast 3 characters");
    }

    @Test
    void test_signUpFailMobileNumberIsNull() {
        InvalidRequest ex =
                assertThrows(
                        InvalidRequest.class,
                        () -> {
                            authService.signupUser("test@test.com", "123456", "Test", null, "Test Address");
                        });
        assertEquals(
                ex.getMessage(),
                "Invalid mobile number");
    }
    @Test
    void test_signUpFailMobileNumberIsInvalid() {
        InvalidRequest ex =
                assertThrows(
                        InvalidRequest.class,
                        () -> {
                            authService.signupUser("test@test.com", "123456", "Test", "98765432", "Test Address");
                        });
        assertEquals(
                ex.getMessage(),
                "Invalid mobile number");
    }

    @Test
    void test_signUpFailInvalidAddress() {
        InvalidRequest ex =
                assertThrows(
                        InvalidRequest.class,
                        () -> {
                            authService.signupUser("test@test.com", "123456", "Test", "9876543210", "A");
                        });
        assertEquals(
                ex.getMessage(),
                "Address should be atleast 10 characters");
    }

    @Test
    void throwsUnauthorizedExceptionWhenTokenIsNull() {
        UnauthorizedException ex = assertThrows(UnauthorizedException.class, ()-> {
            authService.getUserFromToken(null);
        }, "Exception was expected");
        assertEquals(ex.getMessage(), "Invalid Token");
    }

    @Test
    void throwsUnauthorizedExceptionWhenTokenIsInvalid() {
        UnauthorizedException ex = assertThrows(UnauthorizedException.class, ()-> {
            authService.getUserFromToken("fakeToken");
        }, "Exception was expected");
        assertEquals(ex.getMessage(), "Invalid Token");
    }
}
