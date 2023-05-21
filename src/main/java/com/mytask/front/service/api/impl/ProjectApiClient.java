package com.mytask.front.service.api.impl;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.service.api.ProjectApiClientInterface;
import com.mytask.front.service.view.ShowAllTabService;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.HttpClientApi;
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
    public Project createProject(Project project) throws JSONException {
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
                    JSONObject projectJson = new JSONObject(responseBody);
                    JSONObject user = projectJson.getJSONObject("user");

                    int idUser = user.getInt("id");
                    int idProject = projectJson.getInt("id");

                    Project newProject = new Project(
                            projectJson.getString("nom"),
                            projectJson.getString("description"),
                            projectJson.getString("codeJoin"),
                            idProject,
                            idUser
                    );

                    project.setId(idProject);
                    ShowAllTabService.getInstance().getProjects().add(newProject);
                    List<LabelModel> originalLabels = new ArrayList<>(project.getLabels());
                    List<LabelModel> newLabels = new ArrayList<>();
                    for (LabelModel label : originalLabels) {
                        label.setProjectId(project.getId());
                        LabelModel newLabel = LabelApiClient.getInstance().createLabel(label);
                        newLabels.add(newLabel);
                    }
                    project.setLabels(newLabels);
                } else {
                    System.err.println("Project creation failed: Forbidden");
                }
            } else {
                System.err.println("Project creation failed, status code: " + response.statusCode() + "\body: "+ response.body());
            }

            return project;
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
                    System.err.println("Forbidden");
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
        HttpRequest request = HttpClientApi.createPostRequest("http://localhost:3000/api/project/update/" + project.getId(), project.toJSON(), token);
        HttpClientApi.sendRequestAndPrintResponse(httpClient, request);
    }

    @Override
    public void deleteProject(int id) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

}
