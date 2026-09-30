package az.fleettrack.service;
import az.fleettrack.dto.vehicleassignment.VehicleAssignmentCreateRequest;
import az.fleettrack.dto.vehicleassignment.VehicleAssignmentResponse;
import az.fleettrack.entity.Driver;
import az.fleettrack.entity.Vehicle;
import az.fleettrack.entity.VehicleAssignment;
import az.fleettrack.exception.*;
import az.fleettrack.mapper.VehicleAssignmentMapper;
import az.fleettrack.repository.DriverRepository;
import az.fleettrack.repository.VehicleAssignmentRepository;
import az.fleettrack.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VehicleAssignmentService {

    private final VehicleAssignmentRepository assignmentRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final VehicleAssignmentMapper assignmentMapper;

    @Transactional
    public VehicleAssignmentResponse assign(VehicleAssignmentCreateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + request.vehicleId()));

        Driver driver = driverRepository.findById(request.driverId())
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with id: " + request.driverId()));

        if (assignmentRepository.existsByVehicleIdAndUnassignedAtIsNull(vehicle.getId())) {
            throw new VehicleAlreadyAssignedException("Vehicle is already assigned: " + vehicle.getId());
        }

        if (assignmentRepository.existsByDriverIdAndUnassignedAtIsNull(driver.getId())) {
            throw new DriverAlreadyAssignedException("Driver is already assigned: " + driver.getId());
        }

        VehicleAssignment assignment = VehicleAssignment.builder()
                .vehicle(vehicle)
                .driver(driver)
                .assignedAt(LocalDateTime.now())
                .build();

        VehicleAssignment savedAssignment = assignmentRepository.save(assignment);
        return assignmentMapper.toResponse(savedAssignment);
    }

    @Transactional
    public void unassign(Long assignmentId) {
        VehicleAssignment assignment =
                assignmentRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new VehicleAssignmentNotFoundException("Vehicle assignment not found with id: " + assignmentId)
                        );

        if (assignment.getUnassignedAt() != null) {
            return;
        }
        assignment.setUnassignedAt(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public VehicleAssignmentResponse getById(Long id) {
        VehicleAssignment assignment =
                assignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new VehicleAssignmentNotFoundException("Vehicle assignment not found with id: " + id)
                        );
        return assignmentMapper.toResponse(assignment);
    }
}