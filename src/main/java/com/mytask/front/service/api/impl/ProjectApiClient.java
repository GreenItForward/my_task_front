package com.mytask.front.service.api.impl;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.service.api.ProjectApiClientInterface;
import com.mytask.front.service.view.UserService;
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

public class ProjectApiClient implements ProjectApiClientInterface {
    private final HttpClient httpClient;
    private static ProjectApiClient instance;
    private static String token;

    private ProjectApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        token = UserService.getCurrentUser().getToken();
    }

    public static ProjectApiClient getInstance() {
        if (instance == null) {
            instance = new ProjectApiClient();
        }
        return instance;
    }

    @Override
    public void createProject(Project project) throws JSONException {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/project"))
                .POST(HttpRequest.BodyPublishers.ofString(project.toJSON()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
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
                    for (LabelModel label : LabelApiClient.getInstance().getLabels(project)) {
                        label.setProjectId(Integer.parseInt(id));
                        LabelModel newLabel = LabelApiClient.getInstance().createLabel(label);
                        LabelApiClient.getInstance().getLabels(project).add(newLabel);
                    }
                  //  LabelApiClient.getInstance().getLabels(project).clear();
                } else {
                    System.err.println("Project creation failed: Forbidden");
                }
            } else {
                System.err.println("Project creation failed, status code: " + response.statusCode() + "\body: "+ response.body());
            }
        }
    }

    @Override
    public ArrayList<Project> getProjectByUser() throws JSONException {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/project/user"))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
        ArrayList<Project> projects = new ArrayList<>();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (response != null && response.statusCode() == 200) {
                String responseBody = response.body();
                if (!responseBody.contains("Forbidden")) {
                    JSONArray jsonArray = new JSONArray(responseBody);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject project = jsonArray.getJSONObject(i);
                        projects.add(new Project(project.getString("nom"), project.getString("description"), project.getString("codeJoin"), project.getInt("id")));
                    }

                } else {
                    System.err.println("Get project failed: Forbidden");
                }
            } else {
                System.err.println("Get project failed, status code: " + response.statusCode() + "\body: "+ response.body());
            }
        }

        return projects;
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
