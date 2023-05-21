package com.mytask.front.utils;

import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.model.User;
import com.mytask.front.service.ExportService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvExportHelper {
    public CsvExportHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Exporte un tableau de tâches en CSV
     */
    /**
     * Exporte un tableau de tâches en CSV
     */
    public static void exportToCsv(Project project, List<Task> tasks, String filePath) {
        try {
            ExportService.createDirectoryIfNotExist(filePath);

            if (!tasks.isEmpty()) {
                FileWriter csvWriter = new FileWriter(filePath);

                csvWriter.append("Id");
                csvWriter.append(",");
                csvWriter.append("Title");
                csvWriter.append(",");
                csvWriter.append("Details");
                csvWriter.append(",");
                csvWriter.append("Status");
                csvWriter.append(",");
                csvWriter.append("ProjectId");
                csvWriter.append(",");
                csvWriter.append("ProjectName");
                csvWriter.append(",");
                csvWriter.append("Assigned To");
                csvWriter.append("\n");

                for (Task task : tasks) {
                    addTaskToCsv(csvWriter, task, project);
                }

                csvWriter.flush();
                csvWriter.close();
                System.out.println("Tasks exported to CSV successfully: " + filePath);
            } else {
                System.err.println("No tasks found for project: " + project.getNom());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addTaskToCsv(FileWriter csvWriter, Task task, Project project) throws IOException {
        User assignedUser = task.getAssignedTo();
        String assignedUsername = (assignedUser == null || assignedUser.getPrenom().isEmpty()) ? "Unassigned" : assignedUser.getPrenom();

        csvWriter.append(String.valueOf(task.getId()));
        csvWriter.append(",");
        csvWriter.append(task.getTitle());
        csvWriter.append(",");
        csvWriter.append(task.getDetails());
        csvWriter.append(",");
        csvWriter.append(task.getStatus());
        csvWriter.append(",");
        csvWriter.append(String.valueOf(project.getId()));
        csvWriter.append(",");
        csvWriter.append(project.getNom());
        csvWriter.append(",");
        csvWriter.append(assignedUsername);
        csvWriter.append("\n");
    }
}
