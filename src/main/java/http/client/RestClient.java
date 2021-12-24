package http.client;

import http.JavaObjectMapper;
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
    public ReceivedResponse post(String uri, String body) {
        var request = HttpRequest.newBuilder()
                .POST(ofString(body))
                .uri(create(uri))
                .header("Content-Type", "application/json")
                .build();

        var response = httpClient.send(request, ofString());
        logInfo("\nREQUEST: \n" + request + "\n" + "BODY: \n" + body);
        logInfo("\nRESPONSE: \n" + response.body());
        return new ReceivedResponse(response);
    }

    @SneakyThrows
    public ReceivedResponse get(String uri) {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(create(uri))
                .header("Content-Type", "application/json")
                .build();

        var response = httpClient.send(request, ofString());
        logInfo("\nREQUEST: \n" + response.request());
        logInfo("\nRESPONSE: \n" + response.body());
        return new ReceivedResponse(response);
    }

    public class ReceivedResponse {
        private final HttpResponse<String> response;

        private ReceivedResponse(HttpResponse<String> response) {
            this.response = response;
        }

        public <T> T responseAs(Class<T> aClass) {
            return JavaObjectMapper.decode(this.response.body(), aClass);
        }

        public int statusCode() {
            return this.response.statusCode();
        }
    }
}
