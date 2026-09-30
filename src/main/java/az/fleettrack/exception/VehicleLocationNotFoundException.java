package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class VehicleLocationNotFoundException extends BusinessException {

    public VehicleLocationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}