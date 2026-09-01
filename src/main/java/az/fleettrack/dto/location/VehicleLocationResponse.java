package az.fleettrack.dto.location;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VehicleLocationResponse(
        Long vehicleId,
        BigDecimal latitude,
        BigDecimal longitude,
        LocalDateTime timestamp
) {
}