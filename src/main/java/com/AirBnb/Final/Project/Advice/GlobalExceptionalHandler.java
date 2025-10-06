package com.AirBnb.Final.Project.Advice;


import com.AirBnb.Final.Project.Exception.AlreadyExistsException;
import com.AirBnb.Final.Project.Exception.BookingExpiredException;
import com.AirBnb.Final.Project.Exception.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionalHandler {

    ApiError getError(HttpStatus httpStatus,String message){

        ApiError error= ApiError.builder()
                .httpStatus(httpStatus)
                .message(message)
                .build();

        return error;
    }

    ApiResponse<?> getApiResponse(ApiError error){
        return new ApiResponse<>(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiResponse<?>> getResourceNotFoundException(ResourceNotFoundException e){

        ApiError error=getError(HttpStatus.NOT_FOUND,e.getMessage());

        return new ResponseEntity<>(getApiResponse(error),HttpStatus.NOT_FOUND);
    }


    // resource not found exception
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<?>> getResourceNotFoundException(Exception e){

        ApiError error=getError(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());

        return new ResponseEntity<>(getApiResponse(error),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // already exist exception
    @ExceptionHandler(AlreadyExistsException.class)
    ResponseEntity<ApiResponse<?>> getAlreadyExistsException(AlreadyExistsException e){

        ApiError error=getError(HttpStatus.CONFLICT,e.getMessage());

        return new ResponseEntity<>(getApiResponse(error),HttpStatus.CONFLICT);
    }

    // booking expired exception
    @ExceptionHandler(BookingExpiredException.class)
    ResponseEntity<ApiResponse<?>> getBookingExpiredException(BookingExpiredException e){

        ApiError error=getError(HttpStatus.GONE,e.getMessage());

        return new ResponseEntity<>(getApiResponse(error),HttpStatus.GONE);
    }

    // authentication exception
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ApiResponse<?>> getAuthenticationException(AuthenticationException e){

        ApiError error=getError(HttpStatus.UNAUTHORIZED,e.getMessage());

        return new ResponseEntity<>(getApiResponse(error),HttpStatus.UNAUTHORIZED);
    }

    // jwt exception
    @ExceptionHandler(JwtException.class)
    ResponseEntity<ApiResponse<?>> getJwtException(JwtException e){

        ApiError error=getError(HttpStatus.UNAUTHORIZED,e.getMessage());

        return new ResponseEntity<>(getApiResponse(error),HttpStatus.UNAUTHORIZED);
    }

    // access denied exception
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiResponse<?>> getAccessDeniedException(AccessDeniedException e){

        ApiError error=getError(HttpStatus.FORBIDDEN,e.getMessage());

        return new ResponseEntity<>(getApiResponse(error),HttpStatus.FORBIDDEN);
    }

    // method argument exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }




}
