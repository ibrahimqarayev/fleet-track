package az.fleettrack.dto.report;

import az.fleettrack.enums.MaintenanceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MaintenanceReportItem(
        Long id,
        String vehicleInfo,
        LocalDate serviceDate,
        String description,
        BigDecimal cost,
        LocalDate nextServiceDate,
        MaintenanceStatus status
) {}