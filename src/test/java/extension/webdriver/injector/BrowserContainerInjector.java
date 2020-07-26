package extension.webdriver.injector;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static extension.Namespace.INJECTOR_CONTAINER;
import static http.Logger.logInfo;
import static java.time.Duration.ofMinutes;
import static java.util.Arrays.stream;
import static org.apache.commons.io.FileUtils.ONE_MB;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static webdriver.options.CapabilitiesFactory.getDriverCapabilities;

public class BrowserContainerInjector implements TestInstancePostProcessor, BeforeEachCallback, AfterEachCallback {
    private static final Namespace NAMESPACE = create(INJECTOR_CONTAINER);
    private static final String CONTAINER_KEY = "injector.testcontainer";

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        var container = new BrowserWebDriverContainer<>();
        container.withStartupAttempts(3);
        container.withStartupTimeout(ofMinutes(5));
        container.withSharedMemorySize(1024L * ONE_MB);
        container.setPrivilegedMode(true);
        container.withCapabilities(getDriverCapabilities());

        var classField = stream(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .filter(field -> field.getType().isAssignableFrom(BrowserWebDriverContainer.class))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException("Not found injectable <BrowserWebDriverContainer.class> field"));
        classField.setAccessible(true);
        classField.set(testInstance, container);

        context.getStore(NAMESPACE).put(CONTAINER_KEY, container);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        var container = context.getStore(NAMESPACE).get(CONTAINER_KEY, BrowserWebDriverContainer.class);
        container.start();
        logInfo("Container ID : " + container.getContainerId());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        var container = context.getStore(NAMESPACE).get(CONTAINER_KEY, BrowserWebDriverContainer.class);
        container.stop();
        context.getStore(NAMESPACE).remove(CONTAINER_KEY);
    }
}