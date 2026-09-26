package az.fleettrack.dto.maintenance;

import az.fleettrack.enums.MaintenanceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MaintenanceResponse(
        Long id,
        Long vehicleId,
        LocalDate serviceDate,
        String description,
        BigDecimal cost,
        LocalDate nextServiceDate,
        MaintenanceStatus status,
        LocalDateTime createdAt
) {
}