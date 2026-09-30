package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class DriverLicenseAlreadyExistsException extends BusinessException {

    public DriverLicenseAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}