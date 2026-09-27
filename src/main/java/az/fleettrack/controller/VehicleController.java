package az.fleettrack.controller;

import az.fleettrack.dto.common.PageResponse;
import az.fleettrack.dto.vehicle.VehicleCreateRequest;
import az.fleettrack.dto.vehicle.VehicleFilterRequest;
import az.fleettrack.dto.vehicle.VehicleResponse;
import az.fleettrack.dto.vehicle.VehicleUpdateRequest;
import az.fleettrack.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
@Tag(
        name = "Vehicles",
        description = "Vehicle management endpoints"
)
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @Operation(
            summary = "Create vehicle",
            description = "Creates a new vehicle"
    )
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicleService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get vehicle by ID",
            description = "Returns a vehicle by its unique identifier"
    )
    public ResponseEntity<VehicleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @GetMapping
    @Operation(
            summary = "Get all vehicles",
            description = "Returns a paginated and filtered list of vehicles"
    )
    public ResponseEntity<PageResponse<VehicleResponse>> getAll(
            @ModelAttribute VehicleFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(vehicleService.getAll(filter, page, size, sortBy, direction));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update vehicle",
            description = "Updates an existing vehicle by ID"
    )
    public ResponseEntity<VehicleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VehicleUpdateRequest request
    ) {
        return ResponseEntity.ok(vehicleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete vehicle",
            description = "Deletes an existing vehicle by ID"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}