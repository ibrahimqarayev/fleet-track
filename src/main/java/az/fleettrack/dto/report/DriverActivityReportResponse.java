package az.fleettrack.dto.report;

import java.time.LocalDateTime;
import java.util.List;

public record DriverActivityReportResponse(
        LocalDateTime generatedAt,
        long totalDrivers,
        long activeAssignmentsCount,
        long unassignedDriversCount,
        List<DriverReportItem> drivers
) {}