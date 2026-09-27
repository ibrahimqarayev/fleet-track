package az.fleettrack.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final Map<String, Object> data;

    public ErrorResponse(
            int status,
            String error,
            String message,
            String path,
            Map<String, Object> data
    ) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.data = data != null ? data : Map.of();
    }
}