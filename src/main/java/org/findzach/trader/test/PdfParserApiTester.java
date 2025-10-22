package org.findzach.trader.test;

import org.findzach.trader.scheduled.FinancialDisclosureDownloader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
@Component
public class PdfParserApiTester implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final FinancialDisclosureDownloader financialDisclosureDownloader;

    public PdfParserApiTester(FinancialDisclosureDownloader financialDisclosureDownloader) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();

        this.financialDisclosureDownloader = financialDisclosureDownloader;
    }

    @Override
    public void run(String... args) {
        // Replace with your Python API URL (could be localhost:5000 for local testing or your EasyPanel URL)
        String apiBaseUrl = "https://scanner.nojoke.dev";

        System.out.println("Testing PDF Parser API...");

        // Test 1: Health check
        testHealthCheck(apiBaseUrl);

        // Test 2: Parse PDF from URL
        testParseFromUrl(apiBaseUrl);

        System.out.println("=== Starting Financial Disclosure Downloader ===");
        financialDisclosureDownloader.downloadAndExtractDaily();

        // Test 3: Parse from test endpoint
       // testParseFromTest(apiBaseUrl);

        System.out.println("All tests completed!");
    }

    private void testHealthCheck(String apiBaseUrl) {
        try {
            System.out.println("\n=== Testing Health Check ===");
            String url = apiBaseUrl + "/health";

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Response: " + response.getBody());

        } catch (Exception e) {
            System.err.println("Health check failed: " + e.getMessage());
        }
    }

    private void testParseFromUrl(String apiBaseUrl) {
        try {
            System.out.println("\n=== Testing Parse from URL ===");
            String url = apiBaseUrl + "/parse/url";

            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Prepare request body
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("url", "https://disclosures-clerk.house.gov/public_disc/ptr-pdfs/2025/20030630.pdf");

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            // Make the request
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            System.out.println("Status: " + response.getStatusCode());

            // Pretty print JSON response
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            System.out.println("Response:");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));

            // Extract and display transaction count
            if (jsonNode.has("transaction_count")) {
                int count = jsonNode.get("transaction_count").asInt();
                System.out.println("\nFound " + count + " transactions!");
            }

        } catch (Exception e) {
            System.err.println("Parse from URL failed: " + e.getMessage());
        }
    }

    private void testParseFromTest(String apiBaseUrl) {
        try {
            System.out.println("\n=== Testing Parse from Test Endpoint ===");
            String url = apiBaseUrl + "/parse/test";

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            System.out.println("Status: " + response.getStatusCode());

            // Pretty print JSON response
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            System.out.println("Response:");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));

        } catch (Exception e) {
            System.err.println("Parse from test failed: " + e.getMessage());
        }
    }
}
