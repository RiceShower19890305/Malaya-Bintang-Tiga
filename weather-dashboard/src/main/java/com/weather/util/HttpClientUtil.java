package com.weather.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * HTTP client utility for making API requests with timeout and retry logic.
 */
public class HttpClientUtil {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;

    public static String get(String url) throws Exception {
        return getWithRetry(url, 0);
    }

    private static String getWithRetry(String url, int attempt) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("User-Agent", "WeatherDashboard/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else if (response.statusCode() == 429 && attempt < MAX_RETRIES) {
                Thread.sleep(RETRY_DELAY_MS * (attempt + 1));
                return getWithRetry(url, attempt + 1);
            } else {
                throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
            }
        } catch (Exception e) {
            if (attempt < MAX_RETRIES) {
                Thread.sleep(RETRY_DELAY_MS);
                return getWithRetry(url, attempt + 1);
            }
            throw e;
        }
    }
}
