package az.fleettrack.generator;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractPdfReportGenerator {

    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    protected static final Color PRIMARY_COLOR = new Color(33, 37, 41);
    protected static final Color HEADER_BG_COLOR = new Color(230, 235, 240);
    protected static final Color ALTERNATE_ROW_COLOR = new Color(248, 249, 250);

    protected static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, PRIMARY_COLOR);
    protected static final Font SUBTITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
    protected static final Font SECTION_HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, PRIMARY_COLOR);
    protected static final Font TABLE_HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, PRIMARY_COLOR);
    protected static final Font TABLE_BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);

    public byte[] generate() {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setPageEvent(new PdfPageEventHeaderFooter());

            document.open();
            buildPdfDocument(document);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error occurred while generating PDF report", e);
        }

        return out.toByteArray();
    }

    protected abstract void buildPdfDocument(Document document) throws DocumentException;

    protected void addReportHeader(Document document, String title, LocalDateTime generatedAt) throws DocumentException {
        Paragraph titleParagraph = new Paragraph(title, TITLE_FONT);
        titleParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(titleParagraph);

        Paragraph dateParagraph = new Paragraph("Generated At: " + formatDate(generatedAt), SUBTITLE_FONT);
        dateParagraph.setAlignment(Element.ALIGN_LEFT);
        dateParagraph.setSpacingAfter(15f);
        document.add(dateParagraph);
    }

    protected PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, TABLE_HEADER_FONT));
        cell.setBackgroundColor(HEADER_BG_COLOR);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6f);
        return cell;
    }

    protected PdfPCell createCell(String text, int alignment, boolean isEvenRow) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "-", TABLE_BODY_FONT));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        if (isEvenRow) {
            cell.setBackgroundColor(ALTERNATE_ROW_COLOR);
        }
        return cell;
    }

    protected String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "-";
    }

    protected String formatDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMATTER) : "-";
    }
}