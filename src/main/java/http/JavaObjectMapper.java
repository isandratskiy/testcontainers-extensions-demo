package http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public final class JavaObjectMapper {
    private JavaObjectMapper() {
    }

    @SneakyThrows
    public static String serialize(Object object) {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T responseAs(String response, Class<T> aClass) {
        return new ObjectMapper().readValue(response, aClass);
    }
}
