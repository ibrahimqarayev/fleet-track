package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class VehicleAssignmentNotFoundException extends BusinessException {

    public VehicleAssignmentNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}