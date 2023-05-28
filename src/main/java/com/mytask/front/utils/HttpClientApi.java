package com.mytask.front.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientApi {

    public static void sendRequestAndPrintResponse(HttpClient httpClient, HttpRequest request) {
        HttpResponse<String> response = null;
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

    public static HttpRequest createPostRequest(String s, String toJSON, String token) {
        return HttpRequest.newBuilder()
                .uri(URI.create(s))
                .POST(HttpRequest.BodyPublishers.ofString(toJSON))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
    }

    public static void sendRequestAndPrintResponse(HttpRequest request) {
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            if (response != null) {
                System.out.println(response.body());
            }
        }
    }

    public static HttpRequest createDeleteRequest(String s, String token) {
        return HttpRequest.newBuilder()
                .uri(URI.create(s))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
    }
}
