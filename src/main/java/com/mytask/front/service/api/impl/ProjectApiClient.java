package com.mytask.front.service.api.impl;

import com.mytask.front.model.Project;
import com.mytask.front.service.api.ProjectApiClientInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.Duration;

public class ProjectApiClient implements ProjectApiClientInterface {
    private final HttpClient httpClient;

    public ProjectApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
    @Override
    public void createProject(Project project) {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/project"))
                .POST(HttpRequest.BodyPublishers.ofString(project.toJSON()))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (response.statusCode() == 201) {
            System.out.println("Project created successfully");
        } else {
            System.out.println("Project creation failed");
        }
    }

    @Override
    public Project getProjectById(int id) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    @Override
    public void updateProject(Project project) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    @Override
    public void deleteProject(int id) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

}
