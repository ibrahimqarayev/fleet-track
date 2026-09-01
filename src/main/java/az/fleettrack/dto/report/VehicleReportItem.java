package az.fleettrack.dto.report;

import az.fleettrack.enums.VehicleStatus;

public record VehicleReportItem(
        Long id,
        String make,
        String model,
        Integer year,
        String licensePlate,
        VehicleStatus status,
        String assignedDriverName
) {}