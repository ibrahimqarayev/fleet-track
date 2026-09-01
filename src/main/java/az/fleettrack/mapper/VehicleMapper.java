package az.fleettrack.mapper;

import az.fleettrack.dto.vehicle.VehicleCreateRequest;
import az.fleettrack.dto.vehicle.VehicleResponse;
import az.fleettrack.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Vehicle toEntity(VehicleCreateRequest request);

    VehicleResponse toResponse(Vehicle vehicle);
}