package http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public final class JavaObjectMapper {
    private JavaObjectMapper() {
    }

    @SneakyThrows
    public static String encode(Object object) {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T decode(String response, Class<T> aClass) {
        return new ObjectMapper().readValue(response, aClass);
    }
}
