package az.fleettrack.dto.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MaintenanceReportResponse(
        LocalDateTime generatedAt,
        long totalRecords,
        BigDecimal totalCost,
        BigDecimal averageCost,
        List<MaintenanceReportItem> records
) {}