package com.readright.backend.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readright.backend.Records.Analysis;
import com.readright.backend.Records.SentenceAnalysis;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class GetAnalysisService {
    @Value("${hf.api.key}")
    private String hfApiToken;
    private static final String HF_URL = "https://api-inference.huggingface.co/models/facebook/bart-large-mnli";
    private final RestTemplate restTemplate;
    private final SentenceDetector sentenceDetector;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GetAnalysisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        try (InputStream modelIn = getClass().getResourceAsStream("/models/en-sent.bin")) {
            if (modelIn == null) {
                throw new IllegalStateException("Sentence model not found at /models/en-sent.bin");
            }
            SentenceModel model = new SentenceModel(modelIn);
            this.sentenceDetector = new SentenceDetectorME(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Analysis getAnalysis(String text) {
        try {
            String[] sentences = sentenceDetector.sentDetect(text);
            List<String> inputs = List.of(sentences);

            Map<String, Object> requestBody = Map.of(
                    "inputs", inputs,
                    "parameters", Map.of("candidate_labels", List.of("biased", "unbiased", "direct quote"))
            );

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(hfApiToken);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    HF_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());

            List<SentenceAnalysis> results = new ArrayList<>();

            if (root.isArray()) {
                for (JsonNode node : root) {
                    String sentence = node.get("sequence").asText();
                    JsonNode labels = node.get("labels");
                    JsonNode scores = node.get("scores");

                    int bestIdx = 0;
                    double bestScore = -1;
                    for (int i = 0; i < scores.size(); i++) {
                        double sc = scores.get(i).asDouble();
                        if (sc > bestScore) {
                            bestScore = sc;
                            bestIdx = i;
                        }
                    }

                    String label = labels.get(bestIdx).asText();
                    double score = scores.get(bestIdx).asDouble();

                    results.add(new SentenceAnalysis(sentence, label, score));
                }
            }

            return new Analysis(results);

        } catch (HttpClientErrorException e) {
            System.out.println("HF API error: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
