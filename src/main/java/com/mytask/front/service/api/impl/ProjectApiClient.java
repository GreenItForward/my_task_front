package com.mytask.front.service.api.impl;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.service.api.ProjectApiClientInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.Duration;

import static com.mytask.front.configuration.AppConfiguration.bearerToken;

public class ProjectApiClient implements ProjectApiClientInterface {
    private final HttpClient httpClient;
    private static ProjectApiClient instance;

    private ProjectApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public static ProjectApiClient getInstance() {
        if (instance == null) {
            instance = new ProjectApiClient();
        }
        return instance;
    }

    @Override
    public void createProject(Project project) {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/project"))
                .POST(HttpRequest.BodyPublishers.ofString(project.toJSON()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + bearerToken)
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (response != null && response.statusCode() == 201) {
                String responseBody = response.body();
                if (!responseBody.contains("Forbidden")) {
                    String[] responseArray = responseBody.split(",");
                    String id = responseArray[responseArray.length - 1].split(":")[1].replace("}", "").trim();
                    for (LabelModel label : LabelApiClient.getInstance().getLabels()) {
                        label.setProjectId(Integer.parseInt(id));
                        LabelApiClient.getInstance().createLabel(label);
                    }

                    LabelApiClient.getInstance().getLabels().clear();
                } else {
                    System.err.println("Project creation failed: Forbidden");
                }
            } else {
                System.err.println("Project creation failed, status code: " + response.statusCode() + "\body: "+ response.body());
            }
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
