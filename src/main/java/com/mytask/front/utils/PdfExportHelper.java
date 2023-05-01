package com.mytask.front.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.ExportService;

public class PdfExportHelper {
    private PdfExportHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Exporte un tableau de tâches en PDF
     */
    public static void exportToPdf(Project project, List<Task> tasks, String filePath) {
        try {
            ExportService.createDirectoryIfNotExist(filePath);
            if (!tasks.isEmpty()) {
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument, PageSize.A4);

                document.add(new Paragraph("Project: " + project.getNom() + " (" + project.getId() + ")").setBold());
                document.add(new Paragraph("Tasks:"));

                float[] columnWidths = {1, 1, 1};
                Table table = new Table(UnitValue.createPercentArray(columnWidths));
                table.addHeaderCell(new Cell().add(new Paragraph("TODO").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("IN PROGRESS").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("DONE").setBold()));

                addTaskToTable(table, tasks);

                document.add(table);
                document.close();
                System.out.println("Tasks exported to PDF successfully: " + filePath);
            } else {
                System.err.println("No tasks found for project: " + project.getNom());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addTaskToTable(Table table, List<Task> tasks) {
        ArrayList<String> todo = new ArrayList<>();
        ArrayList<String> inProgress = new ArrayList<>();
        ArrayList<String> done = new ArrayList<>();

        for (Task task : tasks) {
            switch (task.getStatus()) {
                case "TODO" -> todo.add(task.getTitle());
                case "IN PROGRESS" -> inProgress.add(task.getTitle());
                case "DONE" -> done.add(task.getTitle());
            }
        }

        int maxRows = Math.max(todo.size(), Math.max(inProgress.size(), done.size()));
        for (int i = 0; i < maxRows; i++) {
            table.addCell(i < todo.size() ? todo.get(i) : "");
            table.addCell(i < inProgress.size() ? inProgress.get(i) : "");
            table.addCell(i < done.size() ? done.get(i) : "");
        }
    }


}