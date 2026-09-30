package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class DriverEmailAlreadyExistsException extends BusinessException {

    public DriverEmailAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}