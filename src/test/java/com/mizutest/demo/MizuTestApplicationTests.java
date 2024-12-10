package com.mizutest.demo;

import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mizutest.demo.entities.Uploads;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MizuTestApplicationTests {

   @LocalServerPort
    private int port;

    private WebTestClient webClient;
    
    

    @BeforeEach
    public void setUp() {
        this.webClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        System.out.println("WebTestClient initialized for base URL: http://localhost:" + port);

    }

    @SuppressWarnings("unchecked")
	@Test
    public void testVideoUploadAndTracking() {
        System.out.println("===== Starting Video Upload and Tracking Test =====");

        System.out.println("Step 1: Uploading video...");
        byte[] jobId = webClient.post()
                .uri("/api/demo/uploadVideo")
                .header("Content-Type", "application/json") // Set Content-Type to application/json
                .bodyValue("{ \"name\": \"Test Video\", \"size\": 5, \"language\": \"English\", \"email\": \"test@example.com\", \"updateType\": \"progress_bar\" }")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id")
                .value(id -> {
                    System.out.println("Video Upload Successful, Job ID: " + id);
                })
                .returnResult()
                .getResponseBody();

        if (jobId == null) {
            throw new RuntimeException("Failed to extract Job ID from response.");
        }

        // Convert the byte array to a String
        String jsonResponse = new String(jobId, StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = null;
		try {
			jsonMap = objectMapper.readValue(jsonResponse, Map.class);
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        Integer extractedJobId = (Integer) jsonMap.get("id"); // Extracts the "id" field
        System.out.println("Extracted Job ID: " + extractedJobId);

        System.out.println("Step 2: Simulating server-side processing delay...");
        try {
            Thread.sleep(30000); // Wait for server-side processing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        
        System.out.println("Step 2: Tracking progress for Job ID: " + extractedJobId);
        EntityExchangeResult<Map> progressResult = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/demo/getStatus")
                        .queryParam("id", Long.valueOf(extractedJobId))
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .returnResult();

        assertNotNull(progressResult, "Progress result should not be null.");
        Map<String, String> progressBody = progressResult.getResponseBody();
        assertNotNull(progressBody, "Response body should not be null.");
        assertThat(progressBody.containsKey("message")).isTrue();

        String progressMessage = progressBody.get("message");
        System.out.println("Progress Status Received: " + progressMessage);

        if (progressMessage.contains("Conversion complete")) {
            System.out.println("Conversion completed successfully!");
            System.out.println("Download Link: " + progressMessage.substring(progressMessage.indexOf("http")));

        } else {
            System.out.println("Conversion still in progress: " + progressMessage);
        }
        System.out.println("===== Test Completed Successfully =====");

    }
    
  
     
    }
