package com.mytask.front.utils;

import java.io.FileNotFoundException;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

public class PdfExportService {
    private PdfExportService() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Exporte un tableau de tâches en PDF
     */
    public static void exportToPdf() {
        try {
            String filename = EPath.PDF.getPath() + EString.getNameTimestamp() + ".pdf";
            PdfWriter writer = new PdfWriter(filename);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4.rotate());

            // en-tête du document
            document.add(new Paragraph("Tableau des tâches"));

            // 3 colonnes (TODO, IN PROGRESS, DONE)
            float[] columnWidths = {1, 1, 1};
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));

            // en-tête des colonnes
            table.addCell(EString.TODO.getString());
            table.addCell(EString.IN_PROGRESS.getString());
            table.addCell(EString.DONE.getString());

            // on ajoute des tâches à chaque colonne
            for (int i = 0; i < 3; i++) {
                table.addCell("[JAVA] Mise en place de javafx " + (i + 1));
                table.addCell("[JAVA] Mise en place de javafx  " + (i + 1));
                table.addCell("[JAVA] Mise en place de javafx  " + (i + 1));
            }

            // on ajoute le tableau au document
            document.add(table);

            System.out.println("Tableau exporté en PDF : " + filename);

            // on ferme le document
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
