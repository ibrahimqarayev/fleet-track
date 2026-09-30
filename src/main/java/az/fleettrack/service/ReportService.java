package az.fleettrack.service;

import az.fleettrack.dto.report.*;
import az.fleettrack.entity.Driver;
import az.fleettrack.entity.MaintenanceRecord;
import az.fleettrack.entity.Vehicle;
import az.fleettrack.entity.VehicleAssignment;
import az.fleettrack.enums.VehicleStatus;
import az.fleettrack.generator.DriverActivityPdfGenerator;
import az.fleettrack.generator.FleetStatusPdfGenerator;
import az.fleettrack.generator.MaintenancePdfGenerator;
import az.fleettrack.repository.DriverRepository;
import az.fleettrack.repository.MaintenanceRecordRepository;
import az.fleettrack.repository.VehicleAssignmentRepository;
import az.fleettrack.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final VehicleRepository vehicleRepository;
    private final VehicleAssignmentRepository vehicleAssignmentRepository;
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final DriverRepository driverRepository;

    @Transactional(readOnly = true)
    public byte[] generateFleetStatusReport() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        // Aktif zimmetli araç-sürücü atamalarını çekiyoruz (unassignedAt == null olanlar)
        List<VehicleAssignment> activeAssignments = vehicleAssignmentRepository.findByUnassignedAtIsNull();

        Map<Long, String> activeDriverMap = activeAssignments.stream()
                .collect(Collectors.toMap(
                        assignment -> assignment.getVehicle().getId(),
                        assignment -> assignment.getDriver().getFirstName() + " " + assignment.getDriver().getLastName(),
                        (existing, replacement) -> existing
                ));

        // Enum statülerine göre sayıları hesaplıyoruz
        Map<VehicleStatus, Long> statusCounts = new EnumMap<>(VehicleStatus.class);
        for (VehicleStatus status : VehicleStatus.values()) {
            statusCounts.put(status, 0L);
        }
        vehicles.forEach(v -> statusCounts.put(v.getStatus(), statusCounts.getOrDefault(v.getStatus(), 0L) + 1));

        // DTO dönüşümü
        List<VehicleReportItem> vehicleItems = vehicles.stream()
                .map(v -> new VehicleReportItem(
                        v.getId(),
                        v.getMake(),
                        v.getModel(),
                        v.getYear(),
                        v.getLicensePlate(),
                        v.getStatus(),
                        activeDriverMap.getOrDefault(v.getId(), "Unassigned")
                ))
                .toList();

        FleetStatusReportResponse response = new FleetStatusReportResponse(
                LocalDateTime.now(),
                vehicles.size(),
                statusCounts,
                vehicleItems
        );

        return new FleetStatusPdfGenerator(response).generate();
    }

    @Transactional(readOnly = true)
    public byte[] generateMaintenanceReport() {
        List<MaintenanceRecord> records = maintenanceRecordRepository.findAllWithVehicle();

        // Toplam Maliyet Hesaplama
        BigDecimal totalCost = records.stream()
                .map(MaintenanceRecord::getCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Ortalama Maliyet Hesaplama
        BigDecimal averageCost = records.isEmpty() ? BigDecimal.ZERO :
                totalCost.divide(BigDecimal.valueOf(records.size()), 2, RoundingMode.HALF_UP);

        // DTO Dönüşümü (MaintenanceRecord Entity -> MaintenanceReportItem DTO)
        List<MaintenanceReportItem> items = records.stream()
                .map(m -> new MaintenanceReportItem(
                        m.getId(),
                        m.getVehicle().getMake() + " " + m.getVehicle().getModel() + " (" + m.getVehicle().getLicensePlate() + ")",
                        m.getServiceDate(),
                        m.getDescription(),
                        m.getCost(),
                        m.getNextServiceDate(),
                        m.getStatus()
                ))
                .toList();

        MaintenanceReportResponse response = new MaintenanceReportResponse(
                LocalDateTime.now(),
                records.size(),
                totalCost,
                averageCost,
                items
        );

        return new MaintenancePdfGenerator(response).generate();
    }

    @Transactional(readOnly = true)
    public byte[] generateDriverActivityReport() {
        List<Driver> drivers = driverRepository.findAll();

        // Aktif zimmetleri araç bazlı çekmiştik, şimdi sürücü bazlı haritalıyoruz (unassignedAt == null)
        List<VehicleAssignment> activeAssignments = vehicleAssignmentRepository.findByUnassignedAtIsNull();
        Map<Long, String> activeVehicleMap = activeAssignments.stream()
                .collect(Collectors.toMap(
                        assignment -> assignment.getDriver().getId(),
                        assignment -> assignment.getVehicle().getMake() + " " + assignment.getVehicle().getModel() + " (" + assignment.getVehicle().getLicensePlate() + ")",
                        (existing, replacement) -> existing
                ));

        List<DriverReportItem> items = drivers.stream()
                .map(d -> {
                    String vehicleInfo = activeVehicleMap.get(d.getId());
                    boolean hasAssignment = vehicleInfo != null;
                    return new DriverReportItem(
                            d.getId(),
                            d.getFirstName() + " " + d.getLastName(),
                            d.getLicenseNumber(),
                            d.getPhone(),
                            hasAssignment ? vehicleInfo : "No Active Vehicle",
                            hasAssignment
                    );
                })
                .toList();

        long activeCount = items.stream().filter(DriverReportItem::hasActiveAssignment).count();
        long unassignedCount = drivers.size() - activeCount;

        DriverActivityReportResponse response = new DriverActivityReportResponse(
                LocalDateTime.now(),
                drivers.size(),
                activeCount,
                unassignedCount,
                items
        );

        return new DriverActivityPdfGenerator(response).generate();
    }
}