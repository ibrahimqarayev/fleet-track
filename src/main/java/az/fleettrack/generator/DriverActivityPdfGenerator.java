package az.fleettrack.generator;

import az.fleettrack.dto.report.DriverActivityReportResponse;
import az.fleettrack.dto.report.DriverReportItem;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;

public class DriverActivityPdfGenerator extends AbstractPdfReportGenerator {

    private final DriverActivityReportResponse reportData;

    public DriverActivityPdfGenerator(DriverActivityReportResponse reportData) {
        this.reportData = reportData;
    }

    @Override
    protected void buildPdfDocument(Document document) throws DocumentException {
        addReportHeader(document, "Driver Activity Report", reportData.generatedAt());
        addSummarySection(document);
        addDriverTable(document);
    }

    private void addSummarySection(Document document) throws DocumentException {
        Paragraph sectionHeader = new Paragraph("Driver Summary", SECTION_HEADER_FONT);
        sectionHeader.setSpacingAfter(8f);
        document.add(sectionHeader);

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(50);
        summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        summaryTable.setSpacingAfter(15f);

        summaryTable.addCell(createHeaderCell("Metric"));
        summaryTable.addCell(createHeaderCell("Value"));

        summaryTable.addCell(createCell("Total Drivers", Element.ALIGN_LEFT, false));
        summaryTable.addCell(createCell(String.valueOf(reportData.totalDrivers()), Element.ALIGN_RIGHT, false));

        summaryTable.addCell(createCell("Drivers with Active Vehicle", Element.ALIGN_LEFT, true));
        summaryTable.addCell(createCell(String.valueOf(reportData.activeAssignmentsCount()), Element.ALIGN_RIGHT, true));

        summaryTable.addCell(createCell("Unassigned Drivers", Element.ALIGN_LEFT, false));
        summaryTable.addCell(createCell(String.valueOf(reportData.unassignedDriversCount()), Element.ALIGN_RIGHT, false));

        document.add(summaryTable);
    }

    private void addDriverTable(Document document) throws DocumentException {
        Paragraph sectionHeader = new Paragraph("Driver List & Assignments", SECTION_HEADER_FONT);
        sectionHeader.setSpacingAfter(8f);
        document.add(sectionHeader);

        // Kolonlar: #, Ad Soyad, Ehliyet No, Telefon, Zimmetli Araç
        PdfPTable table = new PdfPTable(new float[]{1f, 2.5f, 2f, 2f, 3.5f});
        table.setWidthPercentage(100);
        table.setSpacingAfter(10f);

        table.addCell(createHeaderCell("#"));
        table.addCell(createHeaderCell("Full Name"));
        table.addCell(createHeaderCell("License No"));
        table.addCell(createHeaderCell("Phone"));
        table.addCell(createHeaderCell("Assigned Vehicle"));

        int rowIndex = 0;
        for (DriverReportItem driver : reportData.drivers()) {
            boolean isEven = (rowIndex % 2 == 1);

            table.addCell(createCell(String.valueOf(driver.id()), Element.ALIGN_CENTER, isEven));
            table.addCell(createCell(driver.fullName(), Element.ALIGN_LEFT, isEven));
            table.addCell(createCell(driver.licenseNumber(), Element.ALIGN_CENTER, isEven));
            table.addCell(createCell(driver.phone(), Element.ALIGN_CENTER, isEven));
            table.addCell(createCell(driver.assignedVehicleInfo(), Element.ALIGN_LEFT, isEven));

            rowIndex++;
        }

        document.add(table);
    }
}