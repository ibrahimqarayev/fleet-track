package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class DriverAlreadyAssignedException extends BusinessException {

    public DriverAlreadyAssignedException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}