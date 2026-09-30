package az.fleettrack.exception;

import org.springframework.http.HttpStatus;

public class MaintenanceRecordNotFoundException extends BusinessException {

    public MaintenanceRecordNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}