package az.fleettrack.dto.maintenance;

import az.fleettrack.enums.MaintenanceStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MaintenanceUpdateRequest(

        @NotNull
        LocalDate serviceDate,

        @NotBlank
        @Size(max = 500)
        String description,

        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal cost,

        LocalDate nextServiceDate,

        @NotNull
        MaintenanceStatus status
) {
}