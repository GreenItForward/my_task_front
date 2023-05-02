package com.mytask.front.service;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.service.api.impl.TaskLabelApiClient;
import com.mytask.front.utils.CsvExportHelper;
import com.mytask.front.utils.JsonExportHelper;
import com.mytask.front.utils.PdfExportHelper;
import com.mytask.front.utils.enums.EExportType;
import com.mytask.front.utils.enums.EPath;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.mytask.front.utils.enums.EPath.*;

public class ExportService {
    public static void createDirectoryIfNotExist(String filepath) {
        if (isPathExists(filepath)) {
            return;
        }

        try {
            File file = new File(filepath);
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPathExists(String filepath) {
        return new File(filepath).exists();
    }

    public static void createExport(Project project, String format, String extension) {
        String filePath = null;
        try {
            List<Task> tasks = TaskApiClient.getInstance().getTasksByProject(project);
            if (format.equals(String.valueOf(EExportType.JSON))) {
                tasks.forEach(task -> {
                    try {
                        task.setLabels(TaskLabelApiClient.getInstance().getLabelsByTaskId(task.getId(), project.getId()));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            if (extension == null) {
                switch (format) {
                    case "JSON" -> createExport(project, format, EExportType.JSON.getType());
                    case "CSV" -> createExport(project, format, EExportType.CSV.getType());
                    case "PDF" -> createExport(project, format, EExportType.PDF.getType());
                    default -> System.err.println("Format not supported");
                }
            }

            if (!tasks.isEmpty()) {
                String outDirectory = EPath.findByName(format).getPath();
                Files.createDirectories(Paths.get(outDirectory));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String fileName = String.format("%s_%s.%s", project.getNom(), sdf.format(new Date()), extension);
                filePath = outDirectory + File.separator + fileName;

                switch (format) {
                    case "JSON" -> JsonExportHelper.exportToJson(project, tasks, filePath);
                    case "CSV" -> CsvExportHelper.exportToCsv(project, tasks, filePath);
                    case "PDF" -> PdfExportHelper.exportToPdf(project, tasks, filePath);
                    default -> System.err.println("Format not supported");
                }
            } else {
                System.err.println("No tasks found for project: " + project.getNom());
            }
        } catch (IOException | JSONException | AuthException e) {
            e.printStackTrace();
        }

    }

}
