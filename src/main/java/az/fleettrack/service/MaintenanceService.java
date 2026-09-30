package az.fleettrack.service;

import az.fleettrack.dto.common.PageResponse;
import az.fleettrack.dto.maintenance.MaintenanceCreateRequest;
import az.fleettrack.dto.maintenance.MaintenanceResponse;
import az.fleettrack.dto.maintenance.MaintenanceUpdateRequest;
import az.fleettrack.entity.MaintenanceRecord;
import az.fleettrack.entity.Vehicle;
import az.fleettrack.exception.MaintenanceRecordNotFoundException;
import az.fleettrack.exception.VehicleNotFoundException;
import az.fleettrack.mapper.MaintenanceMapper;
import az.fleettrack.repository.MaintenanceRecordRepository;
import az.fleettrack.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceMapper maintenanceMapper;

    @Transactional
    public MaintenanceResponse create(MaintenanceCreateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + request.vehicleId()));
        MaintenanceRecord maintenance = maintenanceMapper.toEntity(request);
        maintenance.setVehicle(vehicle);
        MaintenanceRecord saved = maintenanceRecordRepository.save(maintenance);
        return maintenanceMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public MaintenanceResponse getById(Long id) {
        MaintenanceRecord maintenance =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new MaintenanceRecordNotFoundException("Maintenance record not found with id: " + id)
                        );
        return maintenanceMapper.toResponse(maintenance);
    }

    @Transactional(readOnly = true)
    public PageResponse<MaintenanceResponse> getByVehicleId(Long vehicleId, Pageable pageable) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new VehicleNotFoundException("Vehicle not found with id: " + vehicleId);
        }

        Page<MaintenanceResponse> responsePage = maintenanceRecordRepository
                .findByVehicleId(vehicleId, pageable)
                .map(maintenanceMapper::toResponse);

        return PageResponse.from(responsePage);
    }

    @Transactional
    public MaintenanceResponse update(Long id, MaintenanceUpdateRequest request) {
        MaintenanceRecord maintenance =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new MaintenanceRecordNotFoundException("Maintenance record not found with id: " + id)
                        );

        maintenance.setServiceDate(request.serviceDate());
        maintenance.setDescription(request.description());
        maintenance.setCost(request.cost());
        maintenance.setNextServiceDate(request.nextServiceDate());
        maintenance.setStatus(request.status());
        return maintenanceMapper.toResponse(maintenance);
    }

    @Transactional
    public void delete(Long id) {
        MaintenanceRecord maintenance =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new MaintenanceRecordNotFoundException("Maintenance record not found with id: " + id)
                        );
        maintenanceRecordRepository.delete(maintenance);
    }
}