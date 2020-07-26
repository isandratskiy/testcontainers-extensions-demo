package http.client;

import lombok.SneakyThrows;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static http.Logger.logInfo;
import static java.net.URI.create;
import static java.net.http.HttpClient.Version.HTTP_2;
import static java.net.http.HttpClient.newBuilder;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class RestClient {
    private final HttpClient httpClient = newBuilder()
            .version(HTTP_2)
            .build();

    @SneakyThrows
    public HttpResponse<String> post(String uri, String body) {
        var request = HttpRequest.newBuilder()
                .POST(ofString(body))
                .uri(create(uri))
                .header("Content-Type", "application/json")
                .build();

        var response = httpClient.send(request, ofString());
        logInfo("\nREQUEST: \n" + request + "\n" + "BODY: \n" + body);
        logInfo("\nRESPONSE: \n" + response.body());
        return response;
    }

    @SneakyThrows
    public HttpResponse<String> get(String uri) {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(create(uri))
                .header("Content-Type", "application/json")
                .build();

        var response = httpClient.send(request, ofString());
        logInfo("\nREQUEST: \n" + response.request());
        logInfo("\nRESPONSE: \n" + response.body());
        return response;
    }
}
