package extension.mockserver;

import http.MockProvider;
import lombok.SneakyThrows;
import mock.container.MockServerContainer;
import org.junit.jupiter.api.extension.*;

import static http.Logger.logInfo;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class SetupSingletonMockserverExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {
    private static final String CONTAINER_KEY = "mockserver.container";

    @Override
    public void beforeAll(ExtensionContext context) {
        var mockserver = new MockServerContainer();
        mockserver.withPrivilegedMode(true);
        mockserver.withStartupAttempts(3);
        mockserver.withStartupTimeout(ofMinutes(5));
        mockserver.start();
        context.getStore(GLOBAL).put(CONTAINER_KEY, mockserver);
        logInfo("Container ID : " + mockserver.getContainerId());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        var type = parameterContext.getParameter().getType();
        return MockProvider.class.isAssignableFrom(type);
    }

    @SneakyThrows
    @Override
    public MockProvider resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        var mockServer = extensionContext.getStore(GLOBAL).get(CONTAINER_KEY, MockServerContainer.class);
        return new MockProvider(mockServer);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        context.getStore(GLOBAL).remove(CONTAINER_KEY);
    }
}
