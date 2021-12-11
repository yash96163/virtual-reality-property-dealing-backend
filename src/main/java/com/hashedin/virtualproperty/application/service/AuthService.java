package com.hashedin.virtualproperty.application.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hashedin.virtualproperty.application.entities.User;
import com.hashedin.virtualproperty.application.exceptions.CustomException;
import com.hashedin.virtualproperty.application.exceptions.InvalidRequest;
import com.hashedin.virtualproperty.application.exceptions.UnauthorizedException;
import com.hashedin.virtualproperty.application.repository.UserRepository;
import com.hashedin.virtualproperty.application.response.AuthResponse;
import freemarker.template.TemplateException;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthResponse loginUser(String email, String password) throws MessagingException, TemplateException, IOException {
        this.logger.info(email + " LOGIN REQUEST");
        this.validateData(email, password);

        // find if user is present in database to authenticate
        Optional<User> userContainer = this.userRepository.findById(email);
        if (userContainer.isEmpty()) {
            throw new CustomException("User with email " + email + " does not exist");
        }

        User user = userContainer.get();
        // user exists in database, check if login password is correct
        String hashedPassword = user.getPassword();
        if (!BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified) {
            // invalid credentials for user
            this.logger.warn("FAILED LOGIN ATTEMPT FOR EMAIL " + email);
            throw new CustomException("Invalid login credentials provided");
        }
        // is user is not verified, resend verification email
        this.logger.info("IS USER VERIFIED: " + user.isVerified());
        if (!user.isVerified()) {
            String emailToken = this.getEmailVerificationToken(user.getEmail());
            String tokenUrl = this.getFrontendUrl() + "/verify/" + emailToken;
            this.mailService.sendVerificationEmail(email, tokenUrl);
        }
        // user has entered correct details, generate token and send response
        String token = this.generateJWTToken(user.getEmail());
        return new AuthResponse(user.getEmail(), user.getName(), user.getAddress(), user.getMobile(), token, user.isAdministrator());

    }

    public AuthResponse signupUser(String email, String password, String name, String mobile, String address) throws MessagingException, TemplateException, IOException {
        this.logger.info(email + " SIGNUP REQUEST");
        this.validateSignupData(email, password, name, mobile, address);
        // details are valid till now

        if (this.userRepository.countOfUsersByEmailOrMobile(email, mobile) != 0) {
            // some other user has taken email or mobile, hence unique constraint will be violated
            throw new CustomException("Email or mobile is not unique");
        }
        // sign up user
        String hashedPassword = BCrypt.withDefaults().hashToString(10, password.toCharArray());
        User savedUser = this.userRepository.save(new User(email, hashedPassword, name, mobile, address));
        String token = generateJWTToken(email);
        String emailToken = this.getEmailVerificationToken(email);
        String tokenUrl = this.getFrontendUrl() + "/verify/" + emailToken;
        this.mailService.sendVerificationEmail(email, tokenUrl);
        // return the response
        return new AuthResponse(savedUser.getEmail(), savedUser.getName(), savedUser.getAddress(), savedUser.getMobile(), token, savedUser.isAdministrator());

    }

    public String verifyEmailToken(String token) {
        User user = this.getUserFromEmailToken(token);
        this.logger.info("VERIFYING EMAIL FOR " + user.getEmail());
        user.setVerified(true);
        this.userRepository.save(user);
        return "Validated";
    }

    public void sendPasswordResetEmail(String email) throws MessagingException, TemplateException, IOException {
        this.logger.info("PASSWORD RESET REQUEST FOR EMAIL " + email);
        Optional<User> userOptional = this.userRepository.findById(email);
        if(userOptional.isEmpty()){
            this.logger.info("NO USER WITH EMAIL " + email + " FOR PASSWORD RESET");
            return;
        }
        String emailToken = this.getEmailVerificationToken(email);
        String tokenUrl = this.getFrontendUrl() + "/reset/" + emailToken;
        this.mailService.sendPasswordResetEmail(email, tokenUrl);
    }

    public void resetPassword(String token, String newPassword){
        if(newPassword == null || newPassword.length() < 6) {
            throw new CustomException("Password should have length of atleast 6 characters");
        }
        User user = this.getUserFromEmailToken(token);
        String hashedPassword = BCrypt.withDefaults().hashToString(10, newPassword.toCharArray());
        user.setPassword(hashedPassword);
        this.userRepository.save(user);
    }
    // helper methods

    private void validateSignupData(String email, String password, String name, String mobile, String address) {
        this.validateData(email, password);
        ArrayList<String> invalidData = new ArrayList<String>();
        if (name == null || name.length() < 3) {
            invalidData.add("Name should be atleast 3 characters");
        }
        if (mobile == null) {
            invalidData.add("Invalid mobile number");
        } else {
            // check if mobile number is valid
            try {
                Pattern phoneNumberPattern = Pattern.compile("(0/91)?[7-9][0-9]{9}");
                Matcher phoneNumberMatcher = phoneNumberPattern.matcher(mobile);
                if (!phoneNumberMatcher.find() && phoneNumberMatcher.group().equals(mobile)) {
                    // invalid mobile number
                    throw new Exception("No match found");
                }
            } catch (Exception e) {
                invalidData.add("Invalid mobile number");
            }
        }
        if (address == null || address.length() < 10) {
            invalidData.add("Address should be atleast 10 characters");
        }

        if (invalidData.size() > 0) {
            StringBuilder error = new StringBuilder();
            for (var message : invalidData) {
                error.append(message).append(", ");
            }
            throw new InvalidRequest(error.substring(0, error.length() - 2)); // remove trailing comma
        }
    }

    private void validateData(String email, String password) {
        ArrayList<String> invalidData = new ArrayList<String>();
        if (email == null || !EmailValidator.getInstance().isValid(email)) {
            invalidData.add("Email is invalid");
        }
        if (password == null || password.length() < 6) {
            invalidData.add("Length of Password should be atleast 6 characters");
        }
        if (invalidData.size() > 0) {
            StringBuilder error = new StringBuilder();
            for (var message : invalidData) {
                error.append(message).append(", ");
            }
            throw new InvalidRequest(error.substring(0, error.length() - 2)); // remove trailing comma
        }
    }

    private String generateJWTToken(String email) {
        Algorithm algorithmHS = Algorithm.HMAC256(this.getJwtSecret());
        return JWT.create()
                .withIssuer(this.getIssuer())
                .withClaim("email", email)
                .sign(algorithmHS);
    }

    public User getUserFromToken(String token) {
        // use this method to get the email of user from token
        // it will automatically send error response in case of failure
        if (token == null) {
            throw new UnauthorizedException("Invalid Token");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.getJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.getIssuer())
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            String email = jwt.getClaim("email").asString();
            Optional<User> userOptional = this.userRepository.findById(email);
            if(userOptional.isEmpty()){
                this.logger.warn("USER WITH EMAIL " + email + " NOT FOUND WITH VALID TOKEN. SECRETS MAY BE COMPROMISED");
                throw new CustomException("Invalid Token");
            }
            User user = userOptional.get();
            if(!user.isVerified()){
                throw new CustomException("User is not verified. Use login to get new verification email");
            }
            return user;
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            throw new UnauthorizedException("Invalid Token");
        }
    }

    private String getJwtSecret() {
        // fallback to random string if jwt_secret is not set
        String jwtSecret = System.getenv("JWT_SECRET");
        if (jwtSecret == null || jwtSecret.length() == 0) {
            jwtSecret = "askdjasjdlaskjdlasjdlasjdklasjdaslkfdajhsfgajsgdhasd";
        }
        return jwtSecret;
    }

    private String getIssuer() {
        // fallback issuer if Issuer is not set
        String issuer = System.getenv("ISSUER");
        if (issuer == null || issuer.length() == 0) {
            issuer = "VR_PROPERTY";
        }
        return issuer;
    }

    private String getEmailVerificationJwtSecret() {
        String emailJwtSecret = System.getenv("EMAIL_JWT_SECRET");
        if (emailJwtSecret == null || emailJwtSecret.length() == 0) {
            emailJwtSecret = "qhweiuqydsiuasdibyqiwusayuidyqiywdisiydaiydsiyq";
        }
        return emailJwtSecret;
    }

    private String getEmailVerificationToken(String email) {
        Algorithm algorithmHS = Algorithm.HMAC256(this.getEmailVerificationJwtSecret());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        // token expires after 24 hours
        Date expiryTime = calendar.getTime();
        return JWT.create()
                .withIssuer(this.getIssuer())
                .withClaim("email", email)
                .withClaim("max_date", expiryTime)
                .sign(algorithmHS);
    }

    public User getUserFromEmailToken(String token) {
        // use this method to get the email of user from token
        // it will automatically send error response in case of failure
        if (token == null) {
            throw new UnauthorizedException("Invalid Token");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.getEmailVerificationJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.getIssuer())
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println(jwt.getClaim("max_date").asDate().getTime());
            System.out.println(System.currentTimeMillis());
            if (jwt.getClaim("max_date").asDate().getTime() < System.currentTimeMillis()) {
                throw new UnauthorizedException("Token has expired. Login again to get new token");
            }
            String email = jwt.getClaim("email").asString();
            Optional<User> userOptional = this.userRepository.findById(email);
            if(userOptional.isEmpty()){
                this.logger.warn("USER WITH EMAIL " + email + " NOT FOUND WITH VALID TOKEN. SECRETS MAY BE COMPROMISED");
                throw new CustomException("Invalid Token");
            }
            return userOptional.get();
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            throw new UnauthorizedException("Invalid Token");
        }
    }

    private String getFrontendUrl() {
        String currentUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        if (currentUrl.startsWith("http://localhost")) {
            return "http://localhost:4200";
        }
        return "https://vrpd-frontend-dot-hu18-groupa-angular.et.r.appspot.com";
    }
}
