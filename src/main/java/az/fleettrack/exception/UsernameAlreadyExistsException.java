package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends BusinessException {

    public UsernameAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}