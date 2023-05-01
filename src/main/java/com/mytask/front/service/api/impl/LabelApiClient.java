package com.mytask.front.service.api.impl;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.service.api.LabelApiClientInterface;
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

public class LabelApiClient implements LabelApiClientInterface {
        private final HttpClient httpClient;
        private static LabelApiClient instance;
        private static String token;
        private final ArrayList<LabelModel> labels = new ArrayList<>();

        private LabelApiClient() {
            httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            token = UserService.getCurrentUser().getToken();
        }

        public static LabelApiClient getInstance() {
            if (instance == null) {
                instance = new LabelApiClient();
            }
            return instance;
        }

    @Override
    public LabelModel createLabel(LabelModel label) throws JSONException {
        updateToken(UserService.getCurrentUser().getToken());
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/label"))
                .POST(HttpRequest.BodyPublishers.ofString(label.toJSON()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println(response.statusCode());
            if (response != null && response.statusCode() == 201) {
                String responseBody = response.body();
                if (!responseBody.contains("Forbidden")) {
                    JSONObject jsonObject = new JSONObject(responseBody);

                    label = new LabelModel(jsonObject.getInt("id"), jsonObject.getString("nom"), jsonObject.getString("couleur"), jsonObject.getJSONObject("project").getInt("id"));
                    labels.add(label);
                } else {
                    System.err.println("Get project failed: Forbidden");
                }
            }
        }

        return label;

    }

    @Override
    public void addLabel(LabelModel label) {
        labels.add(label);
    }

    @Override
    public void removeLabel(LabelModel label) {
        labels.remove(label);
    }

    @Override
    public void updateLabel(LabelModel label) {
        updateToken(UserService.getCurrentUser().getToken());
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/label/" + label.getId()))
                .POST(HttpRequest.BodyPublishers.ofString(label.toJSON()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            if (response != null && response.statusCode() == 200) {
                String responseBody = response.body();
                if (!responseBody.contains("Forbidden")) {
                    System.out.println(responseBody);
                } else {
                    System.err.println("Get project failed: Forbidden");
                }
            }
        }
    }

    @Override
    public List<LabelModel> getLabels(Project project) throws JSONException {
        updateToken(UserService.getCurrentUser().getToken());
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/label/" + project.getId()))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            if (response != null && response.statusCode() == 200) {
                String responseBody = response.body();
                if (!responseBody.contains("Forbidden")) {
                    JSONArray jsonArray = new JSONArray(responseBody);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject label = jsonArray.getJSONObject(i);
                        labels.add(new LabelModel(label.getInt("id"), label.getString("nom"), label.getString("couleur"), label.getJSONObject("project").getInt("id")));
                    }

                } else {
                    System.err.println("Get project failed: Forbidden");
                }
            }
        }

        return labels;
    }


    @Override
    public void deleteLabel(LabelModel label) {
        HttpRequest request = HttpClientApi.createDeleteRequest("http://localhost:3000/api/label/" + label.getId(), token);
        HttpClientApi.sendRequestAndPrintResponse(httpClient, request);
        labels.remove(label);
    }

    public void updateToken(String token) {
        this.token = token;
    }

    @Override
    public List<LabelModel> getLabelsByProjectId(int id) throws JSONException {
        updateToken(UserService.getCurrentUser().getToken());
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/label/" + id))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            if (response != null && response.statusCode() == 200) {
                String responseBody = response.body();
                if (!responseBody.contains("Forbidden")) {
                    JSONArray jsonArray = new JSONArray(responseBody);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject label = jsonArray.getJSONObject(i);
                        labels.add(new LabelModel(label.getInt("id"), label.getString("nom"), label.getString("couleur"), label.getJSONObject("project").getInt("id")));
                    }
                } else {
                    System.err.println("Get project failed: Forbidden");
                }
            } else {
                System.err.println("Get project failed, status code: " + response.statusCode() + "\body: " + response.body());
            }
        }

        return labels;
    }

}
