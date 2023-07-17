package com.mytask.front.service.api.impl;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.User;
import com.mytask.front.model.UserSettingModel;
import com.mytask.front.service.api.AuthApiClientInterface;
import com.mytask.front.service.api.UserSettingApiClientInterface;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.enums.EAuthEndpoint;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class UserSettingApiClient implements UserSettingApiClientInterface {
    private final HttpClient httpClient;
    private static UserSettingApiClient instance;
    private String token;


    private UserSettingApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        token = UserService.getCurrentUser().getToken();
    }

    /** Méthode statique afin d'obtenir l'instance unique de ProjectApiClient */
    public static UserSettingApiClient getInstance() {
        if (instance == null) {
            instance = new UserSettingApiClient();
        }
        return instance;
    }


    public void updateToken() {
        this.token = UserService.getCurrentUser().getToken();
    }

    @Override
    public UserSettingModel getUserSettings() throws AuthException {
        HttpResponse<String> response;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/user-setting/get-settings"))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+token)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            JSONObject jsonResponse;
            if(response.body() != null && !response.body().isEmpty()) {
                jsonResponse = new JSONObject(response.body());
            } else {
                System.out.println("The response body is empty or null.");
                return null;
            }
            if (statusCode >= 200 && statusCode < 300) {
                UserSettingModel userSettingModel = new UserSettingModel();
                if (jsonResponse.has("id")) {
                    userSettingModel.setId(jsonResponse.getInt("id"));
                }
                if (jsonResponse.has("user")) {
                    User user = new User();
                    user.setId(jsonResponse.getInt("user"));
                }
                if (jsonResponse.has("background")) {
                    userSettingModel.setBackground(jsonResponse.getString("background").replaceAll("[\\[\\]\"]", ""));
                }
                return userSettingModel;
            }

            if (jsonResponse.has("message")) {
                String result = jsonResponse.getString("message").replaceAll("[\\[\\]\"]", "");
                throw new AuthException(result);
            }
            throw new AuthException(response.body());
        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            throw new AuthException(e.getMessage());
        }
    }

    @Override
    public void postUserSetting(UserSettingModel userSettingModel) throws AuthException {
        HttpResponse<String> response;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/user-setting/post-settings"))
                .POST(HttpRequest.BodyPublishers.ofString(userSettingModel.toJSON()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

}