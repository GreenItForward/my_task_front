package com.mytask.front.service.api.impl;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.TaskLabelApiClientInterface;
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
import java.util.List;

public class TaskLabelApiClient implements TaskLabelApiClientInterface {
    private static TaskLabelApiClient instance;
    private final HttpClient httpClient;

    private int taskId;
    private int labelId;
    private final String token;

    private TaskLabelApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        token = UserService.getCurrentUser().getToken();
    }


    public static TaskLabelApiClient getInstance() {
        if (instance == null) {
            instance = new TaskLabelApiClient();
        }
        return instance;
    }


    @Override
    public void updateLabelToTask(Task task, LabelModel label) {
        HttpResponse<String> response = null;
        this.taskId = task.getId();
        this.labelId = label.getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/task-label"))
                .PUT(HttpRequest.BodyPublishers.ofString(this.toJSON()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
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
    public List<LabelModel> getLabelsByTaskId(int taskId, int projectId) throws JSONException {
        List<LabelModel> labels = new ArrayList<>();
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/task-label/" + taskId))
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
                        labels.add(new LabelModel(label.getInt("id"), label.getString("nom"), label.getString("couleur"), projectId));
                    }

                } else {
                    System.err.println("Get labels by TaskId failed: Forbidden");
                }
            } else {
                System.err.println("Get labels by TaskId failed, status code: " + response.statusCode() + "\body: "+ response.body());
            }
        }
        return labels;
    }



    private String toJSON() {
        return "{" +
                "\"taskId\":" + taskId +
                ", \"labelId\":" + labelId
                + "}";
    }
}
