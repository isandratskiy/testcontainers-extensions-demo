package http.client;

import java.io.IOException;
import java.net.http.*;

import static java.net.URI.*;
import static java.net.http.HttpClient.*;
import static java.net.http.HttpClient.Version.*;
import static java.net.http.HttpRequest.BodyPublishers.*;
import static java.net.http.HttpResponse.BodyHandlers.*;

public class RestClient {
    private final HttpClient httpClient = newBuilder()
            .version(HTTP_2)
            .build();

    public HttpResponse<String> post(String uri, String body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .POST(ofString(body))
                .uri(create(uri))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(
                request, ofString()
        );
    }

    public HttpResponse<String> get(String uri) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(create(uri))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(
                request, ofString()
        );
    }
}
