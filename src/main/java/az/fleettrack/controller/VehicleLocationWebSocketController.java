package az.fleettrack.controller;

import az.fleettrack.dto.location.VehicleLocationRequest;
import az.fleettrack.dto.location.VehicleLocationResponse;
import az.fleettrack.service.VehicleLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class VehicleLocationWebSocketController {

    private final VehicleLocationService vehicleLocationService;

    @MessageMapping("/vehicles/{vehicleId}/location")
    @SendTo("/topic/vehicles/{vehicleId}/location")
    public VehicleLocationResponse updateLocation(
            @DestinationVariable Long vehicleId,
            @Valid VehicleLocationRequest request
    ) {
        return vehicleLocationService.updateLocation(vehicleId, request);
    }
}