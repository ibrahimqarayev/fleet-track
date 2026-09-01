package az.fleettrack.mapper;

import az.fleettrack.dto.maintenance.MaintenanceCreateRequest;
import az.fleettrack.dto.maintenance.MaintenanceResponse;
import az.fleettrack.entity.MaintenanceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MaintenanceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    MaintenanceRecord toEntity(MaintenanceCreateRequest request);

    @Mapping(target = "vehicleId", source = "vehicle.id")
    MaintenanceResponse toResponse(MaintenanceRecord maintenance);
}