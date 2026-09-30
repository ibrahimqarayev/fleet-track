package az.fleettrack.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, WebRequest request) {
        HttpStatus status = ex.getStatus();
        String path = extractPath(request);

        log.warn(
                "Business exception occurred. type={}, status={}, message={}, path={}",
                ex.getClass().getSimpleName(),
                status,
                ex.getMessage(),
                path
        );

        return ResponseEntity
                .status(status)
                .body(buildErrorResponse(
                        status,
                        ex.getMessage(),
                        path,
                        Map.of()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        String path = extractPath(request);

        Map<String, Object> errors = new LinkedHashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        log.warn("Validation failed (request body). path={}, errors={}", path, errors);

        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        "Validation failed",
                        path,
                        errors
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        String path = extractPath(request);

        Map<String, Object> errors = new LinkedHashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(
                    violation.getPropertyPath().toString(),
                    violation.getMessage()
            );
        }

        log.warn("Validation failed (request params). path={}, errors={}", path, errors);

        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        "Validation failed",
                        path,
                        errors
                ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            WebRequest request
    ) {
        String path = extractPath(request);

        log.warn("Authentication failed. path={}", path);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildErrorResponse(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password",
                        path,
                        Map.of()
                ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex, WebRequest request) {
        String path = extractPath(request);

        log.debug("Resource not found. path={}", path);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(
                        HttpStatus.NOT_FOUND,
                        "Resource not found",
                        path,
                        Map.of()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex, WebRequest request) {
        String path = extractPath(request);

        log.error("Unexpected system error occurred. path={}", path, ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Unexpected server error",
                        path,
                        Map.of()
                ));
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message, String path, Map<String, Object> data) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                data
        );
    }

    private String extractPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest
                    .getRequest()
                    .getRequestURI();
        }

        return "";
    }
}