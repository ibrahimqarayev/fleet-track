package az.fleettrack.dto.vehicle;

import az.fleettrack.enums.VehicleStatus;

import java.time.LocalDateTime;

public record VehicleResponse(
        Long id,
        String make,
        String model,
        Integer year,
        String licensePlate,
        VehicleStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}