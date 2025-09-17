package sfera.academyproject.exception;

import sfera.academyproject.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(DataNotFoundException e) {
        logError(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(getMessage(e)));
    }

    @ExceptionHandler({UnauthorizedException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(RuntimeException e) {
        logError(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(getMessage(e)));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<?>> handleForbidden(ForbiddenException e) {
        logError(e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(getMessage(e)));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException e) {
        logError(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(getMessage(e)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException e) {
        logError(e);
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAll(Exception e) {
        logError(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(getMessage(e)));
    }

    // ===== Helper methods =====
    private void logError(Exception e) {
        // Konsolda stack trace chiqadi
        e.printStackTrace();
    }

    private String getMessage(Exception e) {
        return (e.getMessage() != null && !e.getMessage().isBlank())
                ? e.getMessage()
                : e.getClass().getSimpleName();
    }
}
