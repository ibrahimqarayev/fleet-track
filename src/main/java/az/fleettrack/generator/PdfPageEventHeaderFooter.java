package az.fleettrack.generator;

import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.pdf.ColumnText;
import org.openpdf.text.pdf.PdfPageEventHelper;
import org.openpdf.text.pdf.PdfWriter;

import java.awt.*;

public class PdfPageEventHeaderFooter extends PdfPageEventHelper {

    private final Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.ITALIC, Color.GRAY);

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        String footerText = String.format("FleetTrack System Report - Page %d", writer.getPageNumber());

        ColumnText.showTextAligned(
                writer.getDirectContent(),
                Element.ALIGN_CENTER,
                new Phrase(footerText, footerFont),
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10,
                0
        );
    }
}