package com.mytask.front.utils;

import java.io.FileNotFoundException;
import java.util.List;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PdfExportHelper {
    private PdfExportHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Exporte un tableau de tâches en PDF
     */
    public static void exportToPdf(List<VBox> tasksByColumn) {
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
            table.addCell(EString.TODO.toString());
            table.addCell(EString.IN_PROGRESS.toString());
            table.addCell(EString.DONE.toString());

            // Déterminer le nombre maximal de tâches par colonne
            int maxTasks = Math.max(tasksByColumn.get(0).getChildren().size(),
                    Math.max(tasksByColumn.get(1).getChildren().size(),
                            tasksByColumn.get(2).getChildren().size()));

            // Ajout des tâches de chaque colonne
            for (int i = 0; i < maxTasks; i++) {
                for (VBox tasks : tasksByColumn) {
                    if (i < tasks.getChildren().size()) {
                        HBox task = (HBox) tasks.getChildren().get(i);
                        addTaskToTable(task, table);
                    } else {
                        table.addCell("");
                    }
                }
            }

            // on ajoute le tableau au document
            document.add(table);

            System.out.println("Tableau exporté en PDF : " + filename);

            // on ferme le document
            document.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addTaskToTable(HBox task, Table table) {
        HBox etiquette = (HBox) task.getChildren().get(0); // ImageView et VBox
        VBox colorTagVbox = (VBox) etiquette.getChildren().get(0); // Récupération de la VBox

        StringBuilder taskInfo = new StringBuilder();
        for (Node node : colorTagVbox.getChildren()) {
            switch (node.getClass().getSimpleName()) {
                case "Label" -> {
                    // TITLE
                    taskInfo.append(System.lineSeparator());
                    taskInfo.append(((Label) node).getText());
                    taskInfo.append(System.lineSeparator());
                }
                // ASSIGNED TO
                case "TextField" -> taskInfo.append(((TextField) node).getText());
                case "HBox" -> {
                    HBox colorTagHbox = (HBox) node;
                    for (Node hbox : colorTagHbox.getChildren()) {
                        switch (hbox.getClass().getSimpleName()) {
                            case "Label" -> {
                                // COLOR TAG
                                taskInfo.append(((Label) hbox).getText());
                                taskInfo.append(System.lineSeparator());
                            }
                            case "ImageView" -> {
                                // IMAGE
                                ImageView imageView = (ImageView) hbox;
                                taskInfo.append(imageView.getImage().getUrl());
                                taskInfo.append(System.lineSeparator());
                            }
                            case "Button" -> {/* Skip the button, as it is an invisible checkbox*/}
                            default -> {  taskInfo.append(hbox.getId()); } // RECTANGLE ( ID car pas de tooltip )
                        }
                    }
                }
                default -> { /* System.out.println(node); */ }
            }
        }
        table.addCell(taskInfo.toString());
    }
}