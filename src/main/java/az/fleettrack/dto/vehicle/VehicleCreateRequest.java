package az.fleettrack.dto.vehicle;

import az.fleettrack.enums.VehicleStatus;
import jakarta.validation.constraints.*;

public record VehicleCreateRequest(

        @NotBlank
        @Size(max = 100)
        String make,

        @NotBlank
        @Size(max = 100)
        String model,

        @NotNull
        @Min(1900)
        @Max(2100)
        Integer year,

        @NotBlank
        @Size(max = 20)
        String licensePlate,

        @NotNull
        VehicleStatus status
) {
}