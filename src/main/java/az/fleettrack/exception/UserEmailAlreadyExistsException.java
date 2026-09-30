package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class UserEmailAlreadyExistsException extends BusinessException {

    public UserEmailAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}