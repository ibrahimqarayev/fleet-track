package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class DriverNotFoundException extends BusinessException {

    public DriverNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}