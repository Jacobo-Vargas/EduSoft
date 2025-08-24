package com.uniquindio.edu.edusoft.config.exception;

import com.uniquindio.edu.edusoft.model.dto.resposeException.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UsernameNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", ex, req);
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ApiError> handleRedis(RedisConnectionFailureException ex, HttpServletRequest req) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, "No es posible conectar al almacén de tokens (Redis).", ex, req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getAllErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, msg, ex, req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegal(IllegalArgumentException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), ex, req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", ex, req);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String msg, Exception ex, HttpServletRequest req) {
        log.warn("{}", msg, ex); // log completo en servidor
        ApiError body = ApiError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(msg)
                .path(req.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuth(AuthenticationException ex, HttpServletRequest req) {
        // Mensaje neutro para evitar enumeración de usuarios
        return build(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", ex, req);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCreds(BadCredentialsException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", ex, req);
    }
}
