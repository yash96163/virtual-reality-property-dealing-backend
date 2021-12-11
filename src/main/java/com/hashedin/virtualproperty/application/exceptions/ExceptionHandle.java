package com.hashedin.virtualproperty.application.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@ControllerAdvice
public class ExceptionHandle {
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = InvalidRequest.class)
    public ResponseEntity<String> handleInvalidDataException(InvalidRequest ex, WebRequest request) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // user not found in database
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<String> handleUserNotFoundException(CustomException ex, WebRequest request) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // when user has invalid token or no token
    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // generic exception to catch all exceptions
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleAllExceptions(InvalidRequest ex, WebRequest request) {
        logger.error(ex.getMessage());
        return new ResponseEntity<String>("Something went wrong, try again later", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
