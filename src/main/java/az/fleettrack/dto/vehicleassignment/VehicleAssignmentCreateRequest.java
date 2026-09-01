package az.fleettrack.dto.vehicleassignment;

import jakarta.validation.constraints.NotNull;

public record VehicleAssignmentCreateRequest(

        @NotNull
        Long vehicleId,

        @NotNull
        Long driverId
) {
}