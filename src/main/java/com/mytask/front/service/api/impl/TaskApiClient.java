package com.mytask.front.service.api.impl;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.ExportService;
import com.mytask.front.service.api.TaskApiClientInterface;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.CsvExportHelper;
import com.mytask.front.utils.JsonExportHelper;
import com.mytask.front.utils.enums.EExportType;
import com.mytask.front.utils.enums.EStatus;
import com.mytask.front.utils.PdfExportHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mytask.front.utils.enums.EPath.*;

public class TaskApiClient implements TaskApiClientInterface {
    private final HttpClient httpClient;
    private static TaskApiClient instance;
    private final String token;
    private List<Task> tasksList;

    private TaskApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        token = UserService.getCurrentUser().getToken();
    }

    public static TaskApiClient getInstance() {
        if (instance == null) {
            instance = new TaskApiClient();
        }
        return instance;
    }

    @Override
    public Task createTask(Task task) throws JSONException, AuthException {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/task/"))
                .POST(HttpRequest.BodyPublishers.ofString(task.createTaskJson()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        System.out.println(task.createTaskJson());
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println(response.statusCode());
            if (response != null && response.statusCode() == 201) {
                String responseBody = response.body();
                if (!responseBody.contains("Forbidden")) {
                    JSONObject jsonObject = new JSONObject(responseBody);

                    task = new Task(jsonObject.getInt("id"), jsonObject.getString("titre"), jsonObject.getString("description"), EStatus.getStatus(jsonObject.getString("status")), jsonObject.getInt("userId"), jsonObject.getInt("projectId"));
                    tasksList.add(task);
                } else {
                    System.err.println("Get project failed: Forbidden");
                }
            }
        }
        return task;

    }


    @Override
    public Task getTaskById(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateTask(Task task) {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/task"))
                .PUT(HttpRequest.BodyPublishers.ofString(task.toJSON()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (tasksList.contains(task)) {
                tasksList.set(tasksList.indexOf(task), task);
            } else {
                tasksList.add(task);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            if (response != null) {
                System.out.println(response.body());
            }
        }

    }

    @Override
    public List<Task> getTasksByProject(Project project) throws JSONException, AuthException {
        HttpResponse<String> response = null;
        List<Task> tasks = new ArrayList<>();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/task/project/" + project.getId()))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
           // System.out.println("Sending request to http://localhost:3000/api/task/project/" + project.getId()); // DEBUG
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            if (response != null && response.statusCode() == 200) {
                String responseBody = response.body();
                if (!responseBody.contains("Forbidden")) {
                    JSONArray jsonArray = new JSONArray(responseBody);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject label = jsonArray.getJSONObject(i);
                        if (label.getString("deadline").equals("null")) {
                           tasks.add(new Task(label.getInt("id"), label.getString("titre"), label.getString("description"), EStatus.getStatus(label.getString("status")), label.getInt("userId"), label.getInt("projectId")));
                           tasksList = tasks;
                        } else {
                            tasks.add(new Task(label.getInt("id"), label.getString("titre"), label.getString("description"), EStatus.getStatus(label.getString("status")), label.getString("deadline"), label.getInt("userId"), label.getInt("projectId")));
                            tasksList = tasks;
                        }
                    }
                }
            }
        }
        return tasks;
    }

    @Override
    public void exportTasksToPdf(Project project) {
        ExportService.createExport(project, String.valueOf(EExportType.PDF), EExportType.PDF.getType());
    }

    @Override
    public void exportTasksToCsv(Project project) {
        ExportService.createExport(project, String.valueOf(EExportType.CSV), EExportType.CSV.getType());
    }

    @Override
    public void exportTasksToJson(Project project) {
        ExportService.createExport(project, String.valueOf(EExportType.JSON), EExportType.JSON.getType());
    }

    @Override
    public void deleteTask(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Task> getTasksList() {
        return tasksList;
    }

}