package az.fleettrack.service;

import az.fleettrack.dto.location.VehicleLocationRequest;
import az.fleettrack.dto.location.VehicleLocationResponse;
import az.fleettrack.entity.Vehicle;
import az.fleettrack.entity.VehicleLocation;
import az.fleettrack.exception.VehicleLocationNotFoundException;
import az.fleettrack.exception.VehicleNotFoundException;
import az.fleettrack.repository.VehicleLocationRepository;
import az.fleettrack.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VehicleLocationService {

    private final VehicleRepository vehicleRepository;
    private final VehicleLocationRepository vehicleLocationRepository;

    @Transactional
    public VehicleLocationResponse updateLocation(Long vehicleId, VehicleLocationRequest request) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new VehicleNotFoundException("Vehicle not found with id: " + vehicleId)
                );

        VehicleLocation location = VehicleLocation.builder()
                .vehicle(vehicle)
                .latitude(request.latitude())
                .longitude(request.longitude())
                .recordedAt(LocalDateTime.now())
                .build();

        VehicleLocation savedLocation = vehicleLocationRepository.save(location);

        VehicleLocationResponse response =
                new VehicleLocationResponse(
                        vehicleId,
                        savedLocation.getLatitude(),
                        savedLocation.getLongitude(),
                        savedLocation.getRecordedAt()
                );

        return response;
    }

    @Transactional(readOnly = true)
    public VehicleLocationResponse getLatestLocation(Long vehicleId) {

        if (!vehicleRepository.existsById(vehicleId)) {
            throw new VehicleNotFoundException("Vehicle not found with id: " + vehicleId);
        }

        VehicleLocation location =
                vehicleLocationRepository
                        .findTopByVehicleIdOrderByRecordedAtDesc(vehicleId)
                        .orElseThrow(() ->
                                new VehicleLocationNotFoundException(
                                        "Location not found for vehicle id: " + vehicleId
                                )
                        );

        return new VehicleLocationResponse(
                vehicleId,
                location.getLatitude(),
                location.getLongitude(),
                location.getRecordedAt()
        );
    }
}