package az.fleettrack.mapper;

import az.fleettrack.dto.driver.DriverCreateRequest;
import az.fleettrack.dto.driver.DriverResponse;
import az.fleettrack.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DriverMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Driver toEntity(DriverCreateRequest request);

    DriverResponse toResponse(Driver driver);
}