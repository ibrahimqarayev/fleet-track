package az.fleettrack.repository;

import az.fleettrack.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long>, JpaSpecificationExecutor<Driver> {

    boolean existsByEmail(String email);

    boolean existsByLicenseNumber(String licenseNumber);

    Optional<Driver> findByEmail(String email);

    Optional<Driver> findByLicenseNumber(String licenseNumber);
}