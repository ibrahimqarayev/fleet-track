package az.fleettrack.dto.vehicle;

import az.fleettrack.enums.VehicleStatus;

public record VehicleFilterRequest(
        VehicleStatus status,
        Integer year,
        Long driverId
) {
}