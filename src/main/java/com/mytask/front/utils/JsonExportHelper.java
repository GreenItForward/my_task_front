package com.mytask.front.utils;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.AppService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonExportHelper {
    public JsonExportHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Exporte un tableau de tâches en JSON
     */
    public static void exportToJson(Project project, List<Task> tasks, String filePath) {
        try {
            if (!tasks.isEmpty()) {
                JSONArray tasksJsonArray = new JSONArray();

                for (Task task : tasks) {
                    tasksJsonArray.put(addTaskToJson(task, project));
                }

                JSONObject tasksJson = new JSONObject();
                tasksJson.put("tasks", tasksJsonArray);

                try (FileWriter file = new FileWriter(filePath)) {
                    file.write(tasksJson.toString(4));
                    file.flush();
                }

                System.out.println("Tasks exported to JSON successfully: " + filePath);
            } else {
                System.err.println("No tasks found for project: " + project.getNom());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject addTaskToJson(Task task, Project project) throws JSONException {
        JSONObject taskJson = new JSONObject();
        taskJson.put("id", task.getId());
        taskJson.put("title", task.getTitle());
        taskJson.put("details", task.getDetails());
        taskJson.put("status", task.getStatus());
        taskJson.put("projectId", project.getId());
        taskJson.put("projectName", project.getNom());
        taskJson.put("deadline", task.getdeadlineDatePicker().getValue());
        taskJson.put("assignedTo", task.getAssignedTo());
        JSONObject taskJsonObject = new JSONObject();
        for (LabelModel label : task.getLabels()) {
            taskJsonObject.put("id", label.getId());
            taskJsonObject.put("nom", label.getNom());
            taskJsonObject.put("couleur", AppService.colorToHexString(label.getCouleur()));
        }

        taskJson.put("labels", taskJsonObject);

        return taskJson;
    }
}