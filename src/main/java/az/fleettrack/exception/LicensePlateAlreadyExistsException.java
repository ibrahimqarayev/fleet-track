package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class LicensePlateAlreadyExistsException extends BusinessException {

    public LicensePlateAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}