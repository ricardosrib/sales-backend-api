package com.example.sales_system.controller.advice;

import com.example.sales_system.controller.dto.ErrorResponse;
import com.example.sales_system.exception.BadRequestException;
import com.example.sales_system.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final MediaType PROBLEM_JSON = MediaType.parseMediaType("application/problem+json");

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        String traceId = UUID.randomUUID().toString();
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                "about:blank#not-found",
                "Not Found",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                req.getRequestURI(),
                traceId,
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(PROBLEM_JSON).body(body);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        String traceId = UUID.randomUUID().toString();
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                "about:blank#bad-request",
                "Bad Request",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                req.getRequestURI(),
                traceId,
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(PROBLEM_JSON).body(body);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String traceId = UUID.randomUUID().toString();
        Map<String, List<String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        f -> f.getField(),
                        Collectors.mapping(f -> f.getDefaultMessage(), Collectors.toList())
                ));

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                "about:blank#validation-error",
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                "Request validation failed",
                req.getRequestURI(),
                traceId,
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(PROBLEM_JSON).body(body);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        String traceId = UUID.randomUUID().toString();
        Map<String, List<String>> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(
                        v -> v.getPropertyPath().toString(),
                        Collectors.mapping(v -> v.getMessage(), Collectors.toList())
                ));

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                "about:blank#validation-error",
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                "Request validation failed",
                req.getRequestURI(),
                traceId,
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(PROBLEM_JSON).body(body);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        String traceId = UUID.randomUUID().toString();
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                "about:blank#bad-request",
                "Malformed Request",
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON request",
                req.getRequestURI(),
                traceId,
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(PROBLEM_JSON).body(body);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(Exception ex, HttpServletRequest req) {
        String traceId = UUID.randomUUID().toString();
        // Log full details with trace id for diagnostics
        log.error("Unhandled exception (traceId={}) for request {}", traceId, req.getRequestURI(), ex);

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                "about:blank#internal-server-error",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Use traceId to contact support.",
                req.getRequestURI(),
                traceId,
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(PROBLEM_JSON)
                .body(body);
    }
}
