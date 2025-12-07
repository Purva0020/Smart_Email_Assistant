package com.email.writer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;
    private final String apiKey;

    public EmailGeneratorService(WebClient.Builder webClientBuilder,
                                 @Value("${gemini.api.url}") String baseUel,
                                 @Value("${gemini.api.key}") String geminiApiKey) {
        this.apiKey = geminiApiKey;
        this.webClient = webClientBuilder.baseUrl(baseUel)
                .build();
    }

    public String generateEmailReply(EmailRequest emailRequest) {
        //build prompt
        String prompt =buildprompt(emailRequest);

        //prepare raw json body
        String requestBody = String.format("""
                {
                    "contents": [
                      {
                        "parts": [
                          {
                            "text": "%s"
                          }
                        ]
                      }
                    ]
                  }""",prompt);

        //send request
        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.5-flash:generateContent")
                        .build())
                .header("x-goog-api-key",apiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //extract response
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {

        try {
            ObjectMapper mapper=new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildprompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email:");
        if(emailRequest.getTone() !=null && !emailRequest.getTone().isEmpty()){
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone. ");
        }
        prompt.append("Original Email: \n").append(emailRequest.getEmailContent());
        return  prompt.toString();
    }










}


