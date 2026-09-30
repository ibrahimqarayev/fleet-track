package az.fleettrack.service;

import az.fleettrack.dto.common.PageResponse;
import az.fleettrack.dto.vehicle.VehicleCreateRequest;
import az.fleettrack.dto.vehicle.VehicleFilterRequest;
import az.fleettrack.dto.vehicle.VehicleResponse;
import az.fleettrack.dto.vehicle.VehicleUpdateRequest;
import az.fleettrack.entity.Vehicle;
import az.fleettrack.exception.LicensePlateAlreadyExistsException;
import az.fleettrack.exception.VehicleNotFoundException;
import az.fleettrack.mapper.VehicleMapper;
import az.fleettrack.repository.VehicleRepository;
import az.fleettrack.specification.VehicleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponse create(VehicleCreateRequest request) {

        if (vehicleRepository.existsByLicensePlate(request.licensePlate())) {
            throw new LicensePlateAlreadyExistsException(
                    "Vehicle with license plate already exists: " + request.licensePlate()
            );
        }

        Vehicle vehicle = vehicleMapper.toEntity(request);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponse(savedVehicle);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "vehicles", key = "#id")
    public VehicleResponse getById(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));

        return vehicleMapper.toResponse(vehicle);
    }

    @Transactional(readOnly = true)
    public PageResponse<VehicleResponse> getAll(VehicleFilterRequest filter, int page, int size, String sortBy, String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Specification<Vehicle> specification =
                Specification.allOf(
                        VehicleSpecification.hasStatus(filter.status()),
                        VehicleSpecification.hasYear(filter.year()),
                        VehicleSpecification.hasAssignedDriver(
                                filter.driverId()
                        )
                );

        Page<Vehicle> vehiclePage = vehicleRepository.findAll(specification, pageable);

        Page<VehicleResponse> responsePage = vehiclePage.map(vehicleMapper::toResponse);

        return PageResponse.from(responsePage);
    }

    @Transactional
    @CacheEvict(value = "vehicles", key = "#id")
    public VehicleResponse update(Long id, VehicleUpdateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));

        if (!vehicle.getLicensePlate().equals(request.licensePlate())
                && vehicleRepository.existsByLicensePlate(
                request.licensePlate()
        )) {

            throw new LicensePlateAlreadyExistsException(
                    "Vehicle with license plate already exists: " + request.licensePlate()
            );
        }

        vehicle.setMake(request.make());
        vehicle.setModel(request.model());
        vehicle.setYear(request.year());
        vehicle.setLicensePlate(request.licensePlate());
        vehicle.setStatus(request.status());

        return vehicleMapper.toResponse(vehicle);
    }

    @Transactional
    @CacheEvict(value = "vehicles", key = "#id")
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new VehicleNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }
}