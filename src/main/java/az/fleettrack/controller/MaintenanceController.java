package az.fleettrack.controller;

import az.fleettrack.dto.common.PageResponse;
import az.fleettrack.dto.maintenance.MaintenanceCreateRequest;
import az.fleettrack.dto.maintenance.MaintenanceResponse;
import az.fleettrack.dto.maintenance.MaintenanceUpdateRequest;
import az.fleettrack.service.MaintenanceService;
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
@RequestMapping("/api/v1/maintenance-records")
@Tag(
        name = "Maintenance Records",
        description = "Vehicle maintenance record management endpoints"
)
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    @Operation(
            summary = "Create maintenance record",
            description = "Creates a new maintenance record"
    )
    public ResponseEntity<MaintenanceResponse> create(@Valid @RequestBody MaintenanceCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(maintenanceService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get maintenance record by ID",
            description = "Returns a maintenance record by its unique identifier"
    )
    public ResponseEntity<MaintenanceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getById(id));
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(
            summary = "Get maintenance records by Vehicle ID with pagination",
            description = "Returns paginated maintenance records associated with a specific vehicle"
    )
    public ResponseEntity<PageResponse<MaintenanceResponse>> getByVehicleId(
            @PathVariable Long vehicleId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(maintenanceService.getByVehicleId(vehicleId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update maintenance record",
            description = "Updates an existing maintenance record by ID"
    )
    public ResponseEntity<MaintenanceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceUpdateRequest request
    ) {
        return ResponseEntity.ok(maintenanceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete maintenance record",
            description = "Deletes an existing maintenance record by ID"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}