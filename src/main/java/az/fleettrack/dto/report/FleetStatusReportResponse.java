package az.fleettrack.dto.report;

import az.fleettrack.enums.VehicleStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record FleetStatusReportResponse(
        LocalDateTime generatedAt,
        long totalVehicles,
        Map<VehicleStatus, Long> statusCounts,
        List<VehicleReportItem> vehicles
) {}