package az.fleettrack.dto.vehicleassignment;

import java.time.LocalDateTime;

public record VehicleAssignmentResponse(
        Long id,
        Long vehicleId,
        Long driverId,
        LocalDateTime assignedAt,
        LocalDateTime unassignedAt
) {
}