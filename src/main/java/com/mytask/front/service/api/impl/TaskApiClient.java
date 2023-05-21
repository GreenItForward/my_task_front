package com.mytask.front.service.api.impl;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.ExportService;
import com.mytask.front.service.api.TaskApiClientInterface;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.HttpClientApi;
import com.mytask.front.utils.enums.EExportType;
import com.mytask.front.utils.enums.EStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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

                    int id = jsonObject.getInt("id");
                    String titre = jsonObject.getString("titre");
                    String description = jsonObject.getString("description");
                    EStatus status = EStatus.getStatusEnum(jsonObject.getString("status"));
                    int projectId = jsonObject.getInt("projectId");

                    if (!jsonObject.isNull("userId")) {
                        Integer userId = jsonObject.getInt("userId");
                        task = new Task(id, titre, description, status, userId, projectId);
                    } else {
                        task = new Task(id, titre, description, status, projectId);
                    }

                    if (tasksList == null) {
                        tasksList = new ArrayList<>();
                    }

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
        HttpResponse<String> response = null;
        Task task = null;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/task/" + id))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JSONObject jsonObject = new JSONObject(responseBody);

                String titre = jsonObject.getString("titre");
                String description = jsonObject.getString("description");
                EStatus status = EStatus.getStatusEnum(jsonObject.getString("status"));
                int projectId = jsonObject.getInt("projectId");

                if (!jsonObject.isNull("userId")) {
                    Integer userId = jsonObject.getInt("userId");
                    task = new Task(id, titre, description, status, userId, projectId);
                } else {
                    task = new Task(id, titre, description, status, projectId);
                }
            } else {
                System.err.println("Get task failed: " + response.statusCode());
            }
        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return task;
    }

    @Override
    public void updateTask(Task task, Integer userId) {
        if (userId == null) {
            userId = -1;
        }


        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/task"))
                .PUT(HttpRequest.BodyPublishers.ofString(task.toJSON(userId)))
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

    public void updateTask(Task task) {
        updateTask(task, task.getAssignedTo().getId());
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
                        JSONObject task = jsonArray.getJSONObject(i);

                        int id = task.getInt("id");
                        String titre = task.getString("titre");
                        String description = task.getString("description");
                        EStatus status = EStatus.getStatusEnum(task.getString("status"));
                        int projectId = task.getInt("projectId");

                        String deadline = task.optString("deadline", null);
                        Integer userId = null;
                        if (!task.isNull("userId")) {
                            userId = task.getInt("userId");
                        }

                        if (deadline == null && userId == null) {
                            tasks.add(new Task(id, titre, description, status, projectId));
                        } else if (deadline == null) {
                            tasks.add(new Task(id, titre, description, status, userId, projectId));
                        } else if (userId == null) {
                            tasks.add(new Task(id, titre, description, status, deadline, projectId));
                        } else {
                            tasks.add(new Task(id, titre, description, status, deadline, userId, projectId));
                        }
                    }
                    tasksList = tasks;
                }
            }
            return tasks;
        }
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
    public void deleteTask(Task task) {
        HttpRequest request = HttpClientApi.createDeleteRequest("http://localhost:3000/api/task/" + task.getId(), token);
        HttpClientApi.sendRequestAndPrintResponse(request);
    }

    public List<Task> getTasksList() {
        return tasksList;
    }

}