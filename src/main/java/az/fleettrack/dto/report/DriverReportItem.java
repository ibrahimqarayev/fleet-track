package az.fleettrack.dto.report;

public record DriverReportItem(
        Long id,
        String fullName,
        String licenseNumber,
        String phone,
        String assignedVehicleInfo,
        boolean hasActiveAssignment
) {}