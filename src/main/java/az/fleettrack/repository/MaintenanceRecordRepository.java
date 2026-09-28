package az.fleettrack.repository;

import az.fleettrack.entity.MaintenanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    Page<MaintenanceRecord> findByVehicleId(Long vehicleId, Pageable pageable);

    @Query("SELECT m FROM MaintenanceRecord m JOIN FETCH m.vehicle")
    List<MaintenanceRecord> findAllWithVehicle();

}