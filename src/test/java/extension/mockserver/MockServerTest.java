package extension.mockserver;

import http.MockProvider;
import http.client.RestClient;
import http.model.request.NoteModel;
import http.model.response.PersonModel;
import org.junit.jupiter.api.Test;

import static http.JavaObjectMapper.responseAs;
import static http.JavaObjectMapper.serialize;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SetupMockserver
public class MockServerTest {

    @Test
    void shouldReturnPerson(RestClient client, MockProvider provider) {
        var path = "/person";
        var mockResponse = serialize(
                new PersonModel()
                        .name("Peter")
                        .age(38)
                        .position(of("Founder")));

        var mockInstance = provider.createPersonExpectation(path, mockResponse) + "?name=peter";
        var person = responseAs(client.get(mockInstance).body(), PersonModel.class);
        assertEquals(
                "Peter", person.name(), "Expectation returns expected response body");
    }

    @Test
    void shouldValidatePersonNote(RestClient client, MockProvider provider) {
        var path = "/validate";
        var mockRequest = serialize(
                new NoteModel()
                        .name("Joe")
                        .notes("valid"));

        var mockInstance = provider.createPersonNoteExpectation(path, mockRequest);
        var response = client.post(mockInstance, mockRequest);
        assertEquals(
                200, response.statusCode(), "Expectation returns expected response body");
    }
}
