package extension._02_mockserver;

import http.MockProvider;
import http.client.RestClient;
import http.model.request.NoteModel;
import http.model.response.PersonModel;
import org.junit.jupiter.api.Test;

import static http.JavaObjectMapper.encode;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SetupMockserver
public class MockServerTest {

    @Test
    void shouldReturnPerson(RestClient client, MockProvider provider) {
        var path = "/person";
        var mockPerson = encode(
                new PersonModel()
                        .name("Peter")
                        .age(38)
                        .position(of("Founder")));

        var mockInstance = provider.createPersonExpectation(path, mockPerson) + "?name=peter";
        var person = client.get(mockInstance).responseAs(PersonModel.class);
        assertEquals(
                "Peter", person.name(), "Expectation returns expected response body");
    }

    @Test
    void shouldValidatePersonNote(RestClient client, MockProvider provider) {
        var path = "/validate";
        var mockNote = encode(
                new NoteModel()
                        .name("Joe")
                        .notes("valid"));

        var mockInstance = provider.createPersonNoteExpectation(path, mockNote);
        var response = client.post(mockInstance, mockNote);
        assertEquals(
                200, response.statusCode(), "Expectation returns expected response body");
    }
}
