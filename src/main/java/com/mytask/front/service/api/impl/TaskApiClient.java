package com.mytask.front.service.api.impl;

import com.mytask.front.model.Task;
import com.mytask.front.service.api.TaskApiClientInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static com.mytask.front.configuration.AppConfiguration.bearerToken;

public class TaskApiClient implements TaskApiClientInterface {
    private final HttpClient httpClient;
    private static TaskApiClient instance;

    private TaskApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public static TaskApiClient getInstance() {
        if (instance == null) {
            instance = new TaskApiClient();
        }
        return instance;
    }

    @Override
    public void createTask(Task project) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Task getTaskById(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateTask(Task task) {
        HttpResponse<String> response = null;
        task.setId(14);
        System.out.println(task.toJSON());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/task"))
                .PUT(HttpRequest.BodyPublishers.ofString(task.toJSON()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + bearerToken)
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

    @Override
    public void deleteTask(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}