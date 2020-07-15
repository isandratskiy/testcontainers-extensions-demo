package extension.mockserver;

import mock.container.MockServerContainer;
import extension.webdriver.resolver.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import http.client.RestClient;
import http.MockProvider;
import http.model.request.NoteModel;
import http.model.response.PersonModel;

import static java.util.Arrays.*;
import static org.junit.jupiter.api.Assertions.*;
import static http.JavaObjectMapper.*;

@SetupMockserverContainer
public class MockServerTest {

    @Inject
    @Container
    private MockServerContainer mockServer;
    private MockProvider provider;
    private RestClient client;

    @BeforeEach
    void setup() {
        this.client = new RestClient();
        this.provider = new MockProvider(this.mockServer);
    }

    @Test
    void shouldReturnPerson() throws Exception {
        var path = "/person";
        var mockResponse = serialize(
                new PersonModel()
                        .setName("Peter")
                        .setAge(38)
                        .setPosition(asList("Founder", "CTO", "Writer")));

        var mockInstance = provider.createPersonExpectation(path, mockResponse) + "?name=peter";
        var response = responseAs(client.get(mockInstance).body(), PersonModel.class);
        assertEquals(
                "Peter", response.getName(), "Expectation returns expected response body");
    }

    @Test
    void shouldValidatePersonNote() throws Exception {
        var path = "/validate";
        var mockRequest = serialize(
                new NoteModel()
                        .setName("Peter")
                        .setNotes("valid"));

        var mockInstance = provider.createPersonNoteExpectation(path, mockRequest);
        var response = client.post(mockInstance, mockRequest);
        assertEquals(
                200, response.statusCode(), "Expectation returns expected response body");
    }
}
