package extension.mockserver;

import container.MockServerContainer;
import extension.webdriver.resolver.Inject;
import org.junit.jupiter.api.extension.*;
import org.testcontainers.junit.jupiter.Container;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class SetupSingletonMockserverExtension implements BeforeAllCallback, AfterAllCallback, TestInstancePostProcessor {
    private static final String CONTAINER_KEY = "mockserver.container";

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        var mockserver = new MockServerContainer();
        context.getStore(GLOBAL).put(CONTAINER_KEY, mockserver);
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        var mockserver = context.getStore(GLOBAL).get(CONTAINER_KEY, MockServerContainer.class);
        var classField = stream(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Container.class) && field.isAnnotationPresent(Inject.class))
                .filter(field -> field.getType().isAssignableFrom(MockServerContainer.class))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException("Not found injectable Container.class field"));
        classField.setAccessible(true);
        classField.set(testInstance, mockserver);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        context.getStore(GLOBAL).remove(CONTAINER_KEY);
    }
}
