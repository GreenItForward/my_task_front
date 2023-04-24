package com.mytask.front.service.api.impl;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.User;
import com.mytask.front.service.api.AuthApiClientInterface;
import com.mytask.front.service.view.UserService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class AuthApiClient implements AuthApiClientInterface {
    private final HttpClient httpClient;
    private static AuthApiClient instance;

    private AuthApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /** Méthode statique afin d'obtenir l'instance unique de ProjectApiClient */
    public static AuthApiClient getInstance() {
        if (instance == null) {
            instance = new AuthApiClient();
        }
        return instance;
    }

    @Override
    public void authentify(User user, String endpoint) throws AuthException {
        HttpResponse<String> response;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/auth/" + endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(user.toJSON(endpoint)))
                .header("Content-Type", "application/json")
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode >= 200 && statusCode < 300) {
                UserService.setCurrentUser(user);
                UserService.getCurrentUser().setToken(response.body());
                System.out.println(UserService.getCurrentUser().getToken());
                return;
            }

            JSONObject jsonResponse = new JSONObject(response.body());
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
}