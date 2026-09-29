package az.fleettrack.generator;

import az.fleettrack.dto.report.MaintenanceReportItem;
import az.fleettrack.dto.report.MaintenanceReportResponse;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;

public class MaintenancePdfGenerator extends AbstractPdfReportGenerator {

    private final MaintenanceReportResponse reportData;

    public MaintenancePdfGenerator(MaintenanceReportResponse reportData) {
        this.reportData = reportData;
    }

    @Override
    protected void buildPdfDocument(Document document) throws DocumentException {
        addReportHeader(document, "Maintenance & Service Report", reportData.generatedAt());
        addSummarySection(document);
        addMaintenanceTable(document);
    }

    private void addSummarySection(Document document) throws DocumentException {
        Paragraph sectionHeader = new Paragraph("Maintenance Summary", SECTION_HEADER_FONT);
        sectionHeader.setSpacingAfter(8f);
        document.add(sectionHeader);

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(50);
        summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        summaryTable.setSpacingAfter(15f);

        summaryTable.addCell(createHeaderCell("Metric"));
        summaryTable.addCell(createHeaderCell("Value"));

        summaryTable.addCell(createCell("Total Maintenance Records", Element.ALIGN_LEFT, false));
        summaryTable.addCell(createCell(String.valueOf(reportData.totalRecords()), Element.ALIGN_RIGHT, false));

        summaryTable.addCell(createCell("Total Cost", Element.ALIGN_LEFT, true));
        summaryTable.addCell(createCell("$" + reportData.totalCost().toString(), Element.ALIGN_RIGHT, true));

        summaryTable.addCell(createCell("Average Cost per Service", Element.ALIGN_LEFT, false));
        summaryTable.addCell(createCell("$" + reportData.averageCost().toString(), Element.ALIGN_RIGHT, false));

        document.add(summaryTable);
    }

    private void addMaintenanceTable(Document document) throws DocumentException {
        Paragraph sectionHeader = new Paragraph("Maintenance Records", SECTION_HEADER_FONT);
        sectionHeader.setSpacingAfter(8f);
        document.add(sectionHeader);

        // Kolon genişlikleri: #, Araç, Açıklama, Maliyet, Tarih, Sonraki Servis, Durum
        PdfPTable table = new PdfPTable(new float[]{0.8f, 2.2f, 3f, 1.5f, 1.5f, 1.5f, 1.5f});
        table.setWidthPercentage(100);
        table.setSpacingAfter(10f);

        table.addCell(createHeaderCell("#"));
        table.addCell(createHeaderCell("Vehicle"));
        table.addCell(createHeaderCell("Description"));
        table.addCell(createHeaderCell("Cost"));
        table.addCell(createHeaderCell("Service Date"));
        table.addCell(createHeaderCell("Next Service"));
        table.addCell(createHeaderCell("Status"));

        int rowIndex = 0;
        for (MaintenanceReportItem item : reportData.records()) {
            boolean isEven = (rowIndex % 2 == 1);

            table.addCell(createCell(String.valueOf(item.id()), Element.ALIGN_CENTER, isEven));
            table.addCell(createCell(item.vehicleInfo(), Element.ALIGN_LEFT, isEven));
            table.addCell(createCell(item.description(), Element.ALIGN_LEFT, isEven));
            table.addCell(createCell("$" + item.cost().toString(), Element.ALIGN_RIGHT, isEven));
            table.addCell(createCell(formatDate(item.serviceDate()), Element.ALIGN_CENTER, isEven));
            table.addCell(createCell(formatDate(item.nextServiceDate()), Element.ALIGN_CENTER, isEven));
            table.addCell(createCell(item.status().name(), Element.ALIGN_CENTER, isEven));

            rowIndex++;
        }

        document.add(table);
    }
}