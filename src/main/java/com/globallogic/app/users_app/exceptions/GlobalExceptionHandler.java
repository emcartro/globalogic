package com.globallogic.app.users_app.exceptions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.globallogic.app.users_app.model.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String detailMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(". "));

        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), detailMessage);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String detail = "Malformed JSON request. Please check your request body format.";
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();
            detail = String.format("Invalid format for value '%s' at field '%s'. Expected type: %s.",
                    ife.getValue(), ife.getPath().stream()
                            .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "")
                            .collect(Collectors.joining(".")),
                    ife.getTargetType().getSimpleName());
        } else if (ex.getCause() instanceof MismatchedInputException) {
            MismatchedInputException mie = (MismatchedInputException) ex.getCause();
            detail = String.format("Mismatched input for field '%s'. Expected type: %s. Cause: %s",
                    mie.getPath().stream()
                            .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "")
                            .collect(Collectors.joining(".")),
                    mie.getTargetType().getSimpleName(),
                    mie.getOriginalMessage());
        } else if (ex.getMostSpecificCause() != null) {
            detail = "Request body parse error: " + ex.getMostSpecificCause().getMessage();
        }

        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), detail);
    }

    @ExceptionHandler(GlobalogicException.class)
    public ResponseEntity<Object> handleDomusException(GlobalogicException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        int errorCode = httpStatus.value();

        switch (ex.getCode()) {
            case "USER_001":
                httpStatus = HttpStatus.CONFLICT;
                errorCode = 409;
                break;
            case "AUTH_001":
                httpStatus = HttpStatus.UNAUTHORIZED;
                errorCode = 401;
                break;
            case "USER_002":
                httpStatus = HttpStatus.NOT_FOUND;
                errorCode = 404;
                break;
            default:
                httpStatus = HttpStatus.BAD_REQUEST;
                errorCode = 400;
                break;
        }

        return createErrorResponseEntity(httpStatus, errorCode, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return createErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error. Please try again later.");
    }

    private ResponseEntity<Object> createErrorResponseEntity(HttpStatus httpStatus, Integer codigo, String detail) {
        ErrorResponse.ErrorDetail errorDetail = ErrorResponse.ErrorDetail.builder()
                .timestamp(LocalDateTime.now())
                .codigo(codigo)
                .detail(detail)
                .build();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(Collections.singletonList(errorDetail))
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
