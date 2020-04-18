package http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JavaObjectMapper {
    public static String serialize(Object object) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static <T> T responseAs(String response, Class<T> aClass) throws JsonProcessingException {
        return new ObjectMapper().readValue(response, aClass);
    }
}
