package com.example.edp_projeckt.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class ApiFetch {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    public  String fetch(String parameter, String value) {

        String apiKey = "iuO5gg+JbV+MOT5iVOHsuQ==J56FBo2Xz6Cj1NjW";
        String apiUrl = "https://api.api-ninjas.com/v1/exercises?" + parameter + "=" + value;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .setHeader("X-Api-Key", apiKey)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return (response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public  List<Exercise> fetchExercises(String value, String parameter) {
        String responseBody = fetch(value, parameter);
        try {

            TypeReference<List<Exercise>> typeReference = new TypeReference<>() {
            };
            List<Exercise> exercises = OBJECT_MAPPER.readValue(responseBody, typeReference);
            return exercises;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


}