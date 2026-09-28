package az.fleettrack.repository;

import az.fleettrack.entity.VehicleLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleLocationRepository extends JpaRepository<VehicleLocation, Long> {

    Optional<VehicleLocation> findTopByVehicleIdOrderByRecordedAtDesc(Long vehicleId);
}