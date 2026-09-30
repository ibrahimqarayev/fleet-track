package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class VehicleAlreadyAssignedException extends BusinessException {

    public VehicleAlreadyAssignedException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}