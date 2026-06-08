package com.hendersonkleber.backend.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception) {
        var status = HttpStatus.CONFLICT;
        var problem = ProblemDetail.forStatus(status);

        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(exception.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());

        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException exception) {
        var status = HttpStatus.NOT_FOUND;
        var problem = ProblemDetail.forStatus(status);

        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(exception.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());

        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ProblemDetail> handleAuthException(AuthException exception) {
        var status = HttpStatus.BAD_REQUEST;
        var problem = ProblemDetail.forStatus(status);

        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(exception.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());

        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthenticationException(AuthenticationException exception) {
        var status = HttpStatus.UNAUTHORIZED;
        var problem = ProblemDetail.forStatus(status);

        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(exception.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());

        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        var status = HttpStatus.FORBIDDEN;
        var problem = ProblemDetail.forStatus(status);

        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(exception.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());

        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException exception) {
        var status = HttpStatus.NOT_FOUND;
        var problem = ProblemDetail.forStatus(status);

        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(exception.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());

        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception exception) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var problem = ProblemDetail.forStatus(status);

        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(exception.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());

        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(status).body(problem);
    }
}
