package az.fleettrack.controller;

import az.fleettrack.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports", description = "PDF Report Generation endpoints")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/fleet-status")
    @Operation(
            summary = "Generate Fleet Status PDF Report",
            description = "Generates a downloadable PDF report summarizing fleet vehicles, current statuses, and active driver assignments."
    )
    public ResponseEntity<byte[]> getFleetStatusReport() {
        byte[] pdfBytes = reportService.generateFleetStatusReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("fleet-status-report.pdf")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/maintenance")
    @Operation(
            summary = "Generate Maintenance PDF Report",
            description = "Generates a downloadable PDF report summarizing maintenance records, costs, and next service dates."
    )
    public ResponseEntity<byte[]> getMaintenanceReport() {
        byte[] pdfBytes = reportService.generateMaintenanceReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("maintenance-report.pdf")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/driver-activity")
    @Operation(
            summary = "Generate Driver Activity PDF Report",
            description = "Generates a downloadable PDF report summarizing drivers, license numbers, and active vehicle assignments."
    )
    public ResponseEntity<byte[]> getDriverActivityReport() {
        byte[] pdfBytes = reportService.generateDriverActivityReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("driver-activity-report.pdf")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}