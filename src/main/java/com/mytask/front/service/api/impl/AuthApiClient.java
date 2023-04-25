package com.mytask.front.service.api.impl;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.User;
import com.mytask.front.service.api.AuthApiClientInterface;
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
    public String authentify(User user, String endpoint) throws AuthException {
        HttpResponse<String> response;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/auth/" + endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(user.toJSON(endpoint)))
                .header("Content-Type", "application/json")
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode >= 200 && statusCode < 300) {
                System.out.println(response.body());
                return response.body();
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

    @Override
    public User getUser(String token) throws AuthException {
        HttpResponse<String> response;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/auth/getUser"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"token\":\"" + token + "\"}"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+token)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            JSONObject jsonResponse = new JSONObject(response.body());

            if (statusCode >= 200 && statusCode < 300) {
                User user = new User();
                if (jsonResponse.has("email")) {
                    user.setEmail(jsonResponse.getString("email").replaceAll("[\\[\\]\"]", ""));
                }
                if (jsonResponse.has("name")) {
                    user.setNom(jsonResponse.getString("name").replaceAll("[\\[\\]\"]", ""));
                }
                if (jsonResponse.has("firstname")) {
                    user.setPrenom(jsonResponse.getString("firstname").replaceAll("[\\[\\]\"]", ""));
                }
                return user;
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
}