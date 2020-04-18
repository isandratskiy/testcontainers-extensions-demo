package rest;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;

import static java.net.http.HttpClient.*;
import static java.net.http.HttpClient.Version.*;

public class RestClient {
    private final HttpClient httpClient = newBuilder()
            .version(HTTP_2)
            .build();

    public HttpResponse<String> post(String uri, String body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(
                request, HttpResponse.BodyHandlers.ofString()
        );
    }

    public HttpResponse<String> get(String uri) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(
                request, HttpResponse.BodyHandlers.ofString()
        );
    }
}
