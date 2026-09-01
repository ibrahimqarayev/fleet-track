package az.fleettrack.dto.driver;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DriverResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String licenseNumber,
        LocalDate licenseExpiryDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}