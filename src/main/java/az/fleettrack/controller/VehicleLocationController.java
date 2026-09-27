package az.fleettrack.controller;

import az.fleettrack.dto.location.VehicleLocationResponse;
import az.fleettrack.service.VehicleLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleLocationController {

    private final VehicleLocationService vehicleLocationService;

    @GetMapping("/{vehicleId}/location")
    public VehicleLocationResponse getLatestLocation(@PathVariable Long vehicleId) {
        return vehicleLocationService.getLatestLocation(vehicleId);
    }
}