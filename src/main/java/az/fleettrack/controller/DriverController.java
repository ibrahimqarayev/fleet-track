package az.fleettrack.controller;

import az.fleettrack.dto.common.PageResponse;
import az.fleettrack.dto.driver.DriverCreateRequest;
import az.fleettrack.dto.driver.DriverResponse;
import az.fleettrack.dto.driver.DriverUpdateRequest;
import az.fleettrack.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
@Tag(
        name = "Drivers",
        description = "Driver management endpoints"
)
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    @Operation(
            summary = "Create driver",
            description = "Creates a new driver"
    )
    public ResponseEntity<DriverResponse> create(@Valid @RequestBody DriverCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(driverService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get driver by ID",
            description = "Returns a driver by its unique identifier"
    )
    public ResponseEntity<DriverResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getById(id));
    }

    @GetMapping
    @Operation(
            summary = "Get all drivers",
            description = "Returns a paginated list of drivers with optional filtering"
    )
    public ResponseEntity<PageResponse<DriverResponse>> getAll(
            @RequestParam(required = false)
            String firstName,
            @RequestParam(required = false)
            String lastName,
            @RequestParam(required = false)
            String email,
            Pageable pageable
    ) {
        return ResponseEntity.ok(driverService.getAll(firstName, lastName, email, pageable));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update driver",
            description = "Updates an existing driver by ID"
    )
    public ResponseEntity<DriverResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DriverUpdateRequest request
    ) {
        return ResponseEntity.ok(driverService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete driver",
            description = "Deletes an existing driver by ID"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
