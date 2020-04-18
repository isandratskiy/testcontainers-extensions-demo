package rest;

import container.MockServerContainer;
import org.mockserver.client.MockServerClient;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Parameter.param;

public class MockProvider {
    private final MockServerContainer mockServer;

    public MockProvider(MockServerContainer mockServer) {
        this.mockServer = mockServer;
    }

    public String createPersonExpectation(String path, String mockResponse) {
        new MockServerClient(mockServer.getContainerIpAddress(), mockServer.getPort())
                .when(
                        request()
                                .withPath(path)
                                .withQueryStringParameter("name", "peter"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(mockResponse)
                );

        return mockServer.getHost() + path;
    }

    public void createExpectationOverMockServerClient() {
        new MockServerClient(mockServer.getContainerIpAddress(), mockServer.getPort())
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/view/cart")
                                .withQueryStringParameters(
                                        param("cartId", "055CA455-1DF7-45BB-8535-4F83E7266092")
                                )
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("some_response_body")
                );
    }

    public String createPersonNoteExpectation(String path, String mockRequest) {
        new MockServerClient(mockServer.getContainerIpAddress(), mockServer.getPort())
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path)
                                .withHeader("Content-Type", "application/json")
                                .withBody(mockRequest)
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{ \"note\": \"valid\" }")
                );

        return mockServer.getHost() + path;
    }
}
