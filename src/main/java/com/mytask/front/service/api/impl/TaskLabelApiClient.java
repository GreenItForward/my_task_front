package com.mytask.front.service.api.impl;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.TaskLabelApiClientInterface;
import com.mytask.front.service.view.UserService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TaskLabelApiClient implements TaskLabelApiClientInterface {
    private static TaskLabelApiClient instance;
    private final HttpClient httpClient;

    private int taskId;
    private int labelId;
    private final String token;

    private TaskLabelApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        token = UserService.getCurrentUser().getToken();
    }


    public static TaskLabelApiClient getInstance() {
        if (instance == null) {
            instance = new TaskLabelApiClient();
        }
        return instance;
    }


    @Override
    public void updateLabelToTask(Task task, LabelModel label) {
        HttpResponse<String> response = null;
        this.taskId = task.getId();
        this.labelId = label.getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/task-label"))
                .PUT(HttpRequest.BodyPublishers.ofString(this.toJSON()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            if (response != null) {
                System.out.println(response.body());
            }
        }
    }

    private String toJSON() {
        return "{" +
                "\"taskId\":" + taskId +
                ", \"labelId\":" + labelId
                + "}";
    }
}
