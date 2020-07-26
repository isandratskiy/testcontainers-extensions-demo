package extension.mockserver;

import http.client.RestClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public class RestClientResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        var type = parameterContext.getParameter().getType();
        return RestClient.class.isAssignableFrom(type);
    }

    @SneakyThrows
    @Override
    public RestClient resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return new RestClient();
    }
}
