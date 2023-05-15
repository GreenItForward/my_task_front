package com.mytask.front.service.api.impl;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.model.User;
import com.mytask.front.service.api.RoleApiClientInterface;
import com.mytask.front.service.view.ShowAllTabService;
import com.mytask.front.service.view.UserService;
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


public class RoleApiClient implements RoleApiClientInterface {
    private final HttpClient httpClient;
    private static RoleApiClient instance;
    private static String token;

    private RoleApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        token = UserService.getCurrentUser().getToken();
    }

    public static RoleApiClient getInstance() {
        if (instance == null) {
            instance = new RoleApiClient();
        }
        return instance;
    }

    @Override
    public Project joinProject(String codeJoin) throws JSONException {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/user-project/join"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"codeJoin\":\"" + codeJoin + "\"}"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (response == null || response.statusCode() != 201) {
            System.err.println("Error when joining the project, status code: " + response.statusCode());
            System.err.println("body: " + response.body());
            return null;
        }
        String responseBody = response.body();
        if (responseBody.contains("Forbidden")) {
            System.err.println("Get project failed: Forbidden");
            return null;
        }
        JSONObject responseJson = new JSONObject(responseBody);
        JSONObject userJson = responseJson.getJSONObject("user");
        JSONObject projectJson = responseJson.getJSONObject("project");

        int idUser = userJson.getInt("id");
        int idProject = projectJson.getInt("id");
        Project project = new Project(
                projectJson.getString("nom"),
                projectJson.getString("description"),
                projectJson.getString("codeJoin"),
                idProject,
                idUser
        );

        project.setId(idProject);
        ShowAllTabService.getInstance().getProjects().add(project);
        List<LabelModel> originalLabels = new ArrayList<>(project.getLabels());
        List<LabelModel> newLabels = new ArrayList<>();
        for (LabelModel label : originalLabels) {
            label.setProjectId(project.getId());
            LabelModel newLabel = LabelApiClient.getInstance().createLabel(label);
            newLabels.add(newLabel);
        }
        

        /*
    id: number;
    user: User;
    project: Project;
    role: RoleEnum;
        */

        return project;
    }

    @Override
    public void changeRole(int userIdToChange, int projectId, String role) throws JSONException {
        /*
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/user-project/change-role"))
                .PUT(HttpRequest.BodyPublishers.ofString("")
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
                    JSONObject jsonObject = new JSONObject(responseBody);
//                    label = new LabelModel(jsonObject.getInt("id"), jsonObject.getString("nom"), jsonObject.getString("couleur"), jsonObject.getJSONObject("project").getInt("id"));
//                    labels.add(label);
                } else {
                    System.err.println("Get project failed: Forbidden");
                }
            }
        }
         */

        //return label;
    }

    @Override
    public User[] getUsersByProject(int projectId) throws JSONException {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/user-project/get-users-by-project/" + projectId))
                .GET()
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
                    JSONObject jsonObject = new JSONObject(responseBody);
//                    label = new LabelModel(jsonObject.getInt("id"), jsonObject.getString("nom"), jsonObject.getString("couleur"), jsonObject.getJSONObject("project").getInt("id"));
//                    labels.add(label);
                } else {
                    System.err.println("Get project failed: Forbidden");
                }
            }
        }

        return new User[0]; //TODO
    }
}
