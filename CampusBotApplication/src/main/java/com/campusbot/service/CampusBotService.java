package com.campusbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class CampusBotService {

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String fetchAIResponse(String studentQuery) throws IOException, InterruptedException {

        String prompt = "You are CampusBot, a helpful AI FAQ Assistant for university students. "
                + "Answer queries concisely regarding campus life, admissions, or tech support.\n\n"
                + "Student question: " + studentQuery;

        String safePrompt = prompt.replace("\\", "\\\\").replace("\"", "\\\"");

        String payload = "{"
                + "\"contents\": ["
                + "{"
                + "\"parts\": ["
                + "{ \"text\": \"" + safePrompt + "\" }"
                + "]"
                + "}"
                + "]"
                + "}";

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Gemini API failed. Status: " + response.statusCode()
                            + " Response: " + response.body()
            );
        }

        JsonNode rootNode = objectMapper.readTree(response.body());

        JsonNode textNode = rootNode
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text");

        if (!textNode.isMissingNode()) {
            return textNode.asText().trim();
        }

        return "No response could be parsed from Gemini.";
    }
}