package com.mytask.front.service.api.impl;

import com.mytask.front.exception.LoginException;
import com.mytask.front.model.LabelModel;
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
import java.util.ArrayList;


public class AuthApiClient implements AuthApiClientInterface {
        private final HttpClient httpClient;
        private static AuthApiClient instance;

        private ArrayList<LabelModel> labels = new ArrayList<>();

        private AuthApiClient() {
            httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        }

        // Ajouter une méthode statique pour obtenir l'instance unique de ProjectApiClient
        public static AuthApiClient getInstance() {
            if (instance == null) {
                instance = new AuthApiClient();
            }
            return instance;
        }


    @Override
    public void register(User user) {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/auth/register"))
                .POST(HttpRequest.BodyPublishers.ofString(user.toJSON(false)))
                .header("Content-Type", "application/json")
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
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
    public boolean login(User user) throws LoginException {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/auth/login"))
                .POST(HttpRequest.BodyPublishers.ofString(user.toJSON(true)))
                .header("Content-Type", "application/json")
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode >= 200 && statusCode < 300) {
                UserService.getCurrentUser().setToken(response.body());
                System.out.println(UserService.getCurrentUser().getToken());
                return true;
            }

            JSONObject jsonResponse = new JSONObject(response.body());
            if (jsonResponse.has("message")) {
                String result = jsonResponse.getString("message").replaceAll("[\\[\\]\"]", "");
                throw new LoginException(result);
            }
            throw new LoginException(response.body());
        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            throw new LoginException(e.getMessage());
        }
    }
}