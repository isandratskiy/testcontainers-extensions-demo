package extension.webdriver.resolver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;

import static webdriver.options.CapabilitiesFactory.*;
import static java.util.Arrays.stream;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class BrowserContainerInjector implements TestInstancePostProcessor {
    private static final String CONTAINER_KEY = "resolver.container";

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        var container = new BrowserWebDriverContainer<>();
        var classField = stream(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Container.class) && field.isAnnotationPresent(Inject.class))
                .filter(field -> field.getType().isAssignableFrom(BrowserWebDriverContainer.class))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException("Not found injectable Container.class field"));
        classField.setAccessible(true);
        classField.set(testInstance, container.withCapabilities(getDriverCapabilities()));
        context.getStore(GLOBAL).put(CONTAINER_KEY, container);
    }
}
