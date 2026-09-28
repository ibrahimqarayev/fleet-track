package az.fleettrack.repository;

import az.fleettrack.entity.VehicleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleAssignmentRepository extends JpaRepository<VehicleAssignment, Long> {

    boolean existsByVehicleIdAndUnassignedAtIsNull(Long vehicleId);

    boolean existsByDriverIdAndUnassignedAtIsNull(Long driverId);

    List<VehicleAssignment> findByUnassignedAtIsNull();

    Optional<VehicleAssignment> findByVehicleIdAndUnassignedAtIsNull(Long vehicleId);

    Optional<VehicleAssignment> findByDriverIdAndUnassignedAtIsNull(Long driverId);
}