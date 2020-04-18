package extension.mockserver;

import container.MockServerContainer;
import extension.webdriver.resolver.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import rest.client.RestClient;
import rest.MockProvider;
import rest.model.request.NoteModel;
import rest.model.response.PersonModel;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static rest.JavaObjectMapper.*;

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
                        .setPosition(Arrays.asList("Founder", "CTO", "Writer"))
        );

        var mockInstance = provider.createPersonExpectation(path, mockResponse) + "?name=peter";
        var response = responseAs(client.get(mockInstance).body(), PersonModel.class);
        assertEquals(
                "Peter", response.getName(), "Expectation returns expected response body");
    }

    @Test
    void shouldValidatePersonNote() throws Exception {
        var path = "/validate";
        var request = serialize(
                new NoteModel()
                        .setName("Peter")
                        .setNotes("valid")
        );

        var mockInstance = provider.createPersonNoteExpectation(path, request);
        var response = client.post(mockInstance, request);
        assertEquals(
                200, response.statusCode(), "Expectation returns expected response body");
    }
}
