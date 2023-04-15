package com.mytask.front.service.api.impl;

import com.mytask.front.model.LabelModel;
import com.mytask.front.service.api.LabelApiClientInterface;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.mytask.front.configuration.AppConfiguration.bearerToken;

public class LabelApiClient implements LabelApiClientInterface {
        private final HttpClient httpClient;
        private static LabelApiClient instance;

        private ArrayList<LabelModel> labels = new ArrayList<>();

        private LabelApiClient() {
            httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        }

        // Ajouter une méthode statique pour obtenir l'instance unique de ProjectApiClient
        public static LabelApiClient getInstance() {
            if (instance == null) {
                instance = new LabelApiClient();
            }
            return instance;
        }

    @Override
    public void createLabel(LabelModel label) {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/label"))
                .POST(HttpRequest.BodyPublishers.ofString(label.toJSON()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + bearerToken)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            if (response != null) {
                System.out.println(response.body());
            }
        }

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LabelModel> getLabels() {
        return labels;
    }
}
