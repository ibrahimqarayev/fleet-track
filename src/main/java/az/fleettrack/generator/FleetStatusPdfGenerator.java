package az.fleettrack.generator;

import az.fleettrack.dto.report.FleetStatusReportResponse;
import az.fleettrack.dto.report.VehicleReportItem;
import az.fleettrack.enums.VehicleStatus;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;

import java.util.Map;

public class FleetStatusPdfGenerator extends AbstractPdfReportGenerator {

    private final FleetStatusReportResponse reportData;

    public FleetStatusPdfGenerator(FleetStatusReportResponse reportData) {
        this.reportData = reportData;
    }

    @Override
    protected void buildPdfDocument(Document document) throws DocumentException {
        addReportHeader(document, "Fleet Status Report", reportData.generatedAt());
        addSummarySection(document);
        addVehicleTable(document);
    }

    private void addSummarySection(Document document) throws DocumentException {
        Paragraph sectionHeader = new Paragraph("Fleet Summary", SECTION_HEADER_FONT);
        sectionHeader.setSpacingAfter(8f);
        document.add(sectionHeader);

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(50);
        summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        summaryTable.setSpacingAfter(15f);

        summaryTable.addCell(createHeaderCell("Metric"));
        summaryTable.addCell(createHeaderCell("Value"));

        summaryTable.addCell(createCell("Total Vehicles", Element.ALIGN_LEFT, false));
        summaryTable.addCell(createCell(String.valueOf(reportData.totalVehicles()), Element.ALIGN_RIGHT, false));

        int rowCount = 1;
        for (Map.Entry<VehicleStatus, Long> entry : reportData.statusCounts().entrySet()) {
            boolean isEven = (rowCount % 2 == 1);
            summaryTable.addCell(createCell(entry.getKey().name() + " Status", Element.ALIGN_LEFT, isEven));
            summaryTable.addCell(createCell(String.valueOf(entry.getValue()), Element.ALIGN_RIGHT, isEven));
            rowCount++;
        }

        document.add(summaryTable);
    }

    private void addVehicleTable(Document document) throws DocumentException {
        Paragraph sectionHeader = new Paragraph("Vehicle List", SECTION_HEADER_FONT);
        sectionHeader.setSpacingAfter(8f);
        document.add(sectionHeader);

        PdfPTable table = new PdfPTable(new float[]{1f, 2f, 2f, 1.2f, 2f, 1.8f, 2.5f});
        table.setWidthPercentage(100);
        table.setSpacingAfter(10f);

        table.addCell(createHeaderCell("#"));
        table.addCell(createHeaderCell("Make"));
        table.addCell(createHeaderCell("Model"));
        table.addCell(createHeaderCell("Year"));
        table.addCell(createHeaderCell("Plate"));
        table.addCell(createHeaderCell("Status"));
        table.addCell(createHeaderCell("Assigned Driver"));

        int rowIndex = 0;
        for (VehicleReportItem vehicle : reportData.vehicles()) {
            boolean isEven = (rowIndex % 2 == 1);

            table.addCell(createCell(String.valueOf(vehicle.id()), Element.ALIGN_CENTER, isEven));
            table.addCell(createCell(vehicle.make(), Element.ALIGN_LEFT, isEven));
            table.addCell(createCell(vehicle.model(), Element.ALIGN_LEFT, isEven));
            table.addCell(createCell(String.valueOf(vehicle.year()), Element.ALIGN_CENTER, isEven));
            table.addCell(createCell(vehicle.licensePlate(), Element.ALIGN_LEFT, isEven));
            table.addCell(createCell(vehicle.status().name(), Element.ALIGN_CENTER, isEven));
            table.addCell(createCell(vehicle.assignedDriverName(), Element.ALIGN_LEFT, isEven));

            rowIndex++;
        }

        document.add(table);
    }
}