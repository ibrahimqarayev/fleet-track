package az.fleettrack.service;

import az.fleettrack.dto.common.PageResponse;
import az.fleettrack.dto.driver.DriverCreateRequest;
import az.fleettrack.dto.driver.DriverResponse;
import az.fleettrack.dto.driver.DriverUpdateRequest;
import az.fleettrack.entity.Driver;
import az.fleettrack.exception.DriverEmailAlreadyExistsException;
import az.fleettrack.exception.DriverLicenseAlreadyExistsException;
import az.fleettrack.exception.DriverNotFoundException;
import az.fleettrack.mapper.DriverMapper;
import az.fleettrack.repository.DriverRepository;
import az.fleettrack.specification.DriverSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Transactional
    public DriverResponse create(DriverCreateRequest request) {

        if (driverRepository.existsByEmail(request.email())) {
            throw new DriverEmailAlreadyExistsException("Driver with email already exists: " + request.email());
        }

        if (driverRepository.existsByLicenseNumber(request.licenseNumber())) {
            throw new DriverLicenseAlreadyExistsException(
                    "Driver with license number already exists: " + request.licenseNumber()
            );
        }

        Driver driver = driverMapper.toEntity(request);
        Driver savedDriver = driverRepository.save(driver);

        return driverMapper.toResponse(savedDriver);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "drivers", key = "#id")
    public DriverResponse getById(Long id) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with id: " + id));

        return driverMapper.toResponse(driver);
    }

    @Transactional(readOnly = true)
    public PageResponse<DriverResponse> getAll(String firstName, String lastName, String email, Pageable pageable) {
        Specification<Driver> specification = Specification
                .where(DriverSpecification.hasFirstName(firstName))
                .and(DriverSpecification.hasLastName(lastName))
                .and(DriverSpecification.hasEmail(email));

        Page<Driver> page = driverRepository.findAll(specification, pageable);

        Page<DriverResponse> responsePage = page.map(driverMapper::toResponse);

        return PageResponse.from(responsePage);
    }

    @Transactional
    @CacheEvict(value = "drivers", key = "#id")
    public DriverResponse update(Long id, DriverUpdateRequest request) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with id: " + id));

        if (!driver.getEmail().equals(request.email()) && driverRepository.existsByEmail(request.email())) {
            throw new DriverEmailAlreadyExistsException("Driver with email already exists: " + request.email());
        }

        if (!driver.getLicenseNumber().equals(
                request.licenseNumber()
        )
                && driverRepository.existsByLicenseNumber(
                request.licenseNumber()
        )) {

            throw new DriverLicenseAlreadyExistsException(
                    "Driver with license number already exists: " + request.licenseNumber()
            );
        }

        driver.setFirstName(request.firstName());
        driver.setLastName(request.lastName());
        driver.setEmail(request.email());
        driver.setPhone(request.phone());
        driver.setLicenseNumber(request.licenseNumber());
        driver.setLicenseExpiryDate(request.licenseExpiryDate());

        return driverMapper.toResponse(driver);
    }

    @Transactional
    @CacheEvict(value = "drivers", key = "#id")
    public void delete(Long id) {

        if (!driverRepository.existsById(id)) {
            throw new DriverNotFoundException("Driver not found with id: " + id);
        }

        driverRepository.deleteById(id);
    }
}