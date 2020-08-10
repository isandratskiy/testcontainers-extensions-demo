package extension._01_webdriver.injector;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.DefaultRecordingFileFactory;
import org.testcontainers.lifecycle.TestDescription;

import java.io.File;

import static extension.Namespace.INJECTOR_CONTAINER;
import static http.Logger.logInfo;
import static java.time.Duration.ofMinutes;
import static java.util.Arrays.stream;
import static org.apache.commons.io.FileUtils.ONE_MB;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;
import static webdriver.options.CapabilitiesFactory.getDriverCapabilities;

public class BrowserContainerInjector implements TestInstancePostProcessor, BeforeEachCallback, AfterEachCallback {
    private static final Namespace NAMESPACE = create(INJECTOR_CONTAINER);
    private String CONTAINER_KEY;

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        var container = new BrowserWebDriverContainer<>();
        container.withStartupAttempts(3);
        container.withStartupTimeout(ofMinutes(5));
        container.withRecordingMode(RECORD_ALL, new File("build"));
        container.withRecordingFileFactory(new DefaultRecordingFileFactory());
        container.withSharedMemorySize(1024L * ONE_MB);
        container.withPrivilegedMode(true);
        container.withCapabilities(getDriverCapabilities());

        var classField = stream(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .filter(field -> field.getType().isAssignableFrom(BrowserWebDriverContainer.class))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException("Not found injectable <BrowserWebDriverContainer.class> field"));

        classField.setAccessible(true);
        classField.set(testInstance, container);

        container.start();
        this.CONTAINER_KEY = container.getContainerId();

        context.getStore(NAMESPACE).put(CONTAINER_KEY, container);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        var container = context.getStore(NAMESPACE).get(CONTAINER_KEY, BrowserWebDriverContainer.class);
        logInfo("Container ID : " + container.getContainerId());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        var container = context.getStore(NAMESPACE).get(CONTAINER_KEY, BrowserWebDriverContainer.class);
        container.afterTest(
                new TestDescription() {
                    @Override
                    public String getTestId() {
                        return context.getUniqueId();
                    }

                    @Override
                    public String getFilesystemFriendlyName() {
                        return context.getRequiredTestMethod().getName();
                    }
                },
                context.getExecutionException());

        container.stop();
        context.getStore(NAMESPACE).remove(CONTAINER_KEY);
    }
}