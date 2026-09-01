package az.fleettrack.mapper;
import az.fleettrack.dto.vehicleassignment.VehicleAssignmentResponse;
import az.fleettrack.entity.VehicleAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VehicleAssignmentMapper {

    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "driverId", source = "driver.id")
    VehicleAssignmentResponse toResponse(VehicleAssignment assignment);
}