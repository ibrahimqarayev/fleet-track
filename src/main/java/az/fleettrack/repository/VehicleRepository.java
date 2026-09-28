package az.fleettrack.repository;

import az.fleettrack.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    boolean existsByLicensePlate(String licensePlate);

    @Query("""
            SELECT v
            FROM Vehicle v
            WHERE EXISTS (
                SELECT 1
                FROM VehicleAssignment va
                WHERE va.vehicle = v
                  AND va.driver.id = :driverId
                  AND va.unassignedAt IS NULL
            )
            """)
    Page<Vehicle> findByAssignedDriver(
            @Param("driverId") Long driverId,
            Pageable pageable
    );
}