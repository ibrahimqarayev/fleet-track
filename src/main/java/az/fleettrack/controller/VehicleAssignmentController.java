package az.fleettrack.controller;

import az.fleettrack.dto.vehicleassignment.VehicleAssignmentCreateRequest;
import az.fleettrack.dto.vehicleassignment.VehicleAssignmentResponse;
import az.fleettrack.service.VehicleAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicle-assignments")
@Tag(
        name = "Vehicle Assignments",
        description = "Vehicle assignment management endpoints"
)
public class VehicleAssignmentController {

    private final VehicleAssignmentService assignmentService;

    @PostMapping
    @Operation(
            summary = "Assign vehicle to driver",
            description = "Assigns a vehicle to a driver"
    )
    public ResponseEntity<VehicleAssignmentResponse> assign(
            @Valid @RequestBody VehicleAssignmentCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assignmentService.assign(request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get vehicle assignment by ID",
            description = "Returns a vehicle assignment by its unique identifier"
    )
    public ResponseEntity<VehicleAssignmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getById(id));
    }

    @PatchMapping("/{id}/unassign")
    @Operation(
            summary = "Unassign vehicle",
            description = "Removes the vehicle assignment by assignment ID"
    )
    public ResponseEntity<Void> unassign(@PathVariable Long id) {
        assignmentService.unassign(id);
        return ResponseEntity.noContent().build();
    }
}