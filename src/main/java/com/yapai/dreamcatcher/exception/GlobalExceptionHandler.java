package com.yapai.dreamcatcher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationCredentialsNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Giriş Yapmalısınız");
    }

}
