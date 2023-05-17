package com.mytask.front.service.api.impl;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.model.User;
import com.mytask.front.model.UserProject;
import com.mytask.front.service.api.RoleApiClientInterface;
import com.mytask.front.service.view.ShowAllTabService;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.enums.ERole;
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
    public UserProject joinProject(String codeJoin) throws JSONException {
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
        System.out.println(responseJson);
        JSONObject userJson = responseJson.getJSONObject("user");
        JSONObject projectJson = responseJson.getJSONObject("project");

        Project project = new Project(
                projectJson.getString("nom"),
                projectJson.getString("description")
        );
        project.setId(projectJson.getInt("id"));

        int idUser = userJson.getInt("id");
        ERole role = ERole.findByName(responseJson.getString("role"));
        User user = new User(
                idUser,
                userJson.getString("email"),
                userJson.getString("name"),
                userJson.getString("firstname"),
                "",
                role,
                token
        );

        return new UserProject(user, project);
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
