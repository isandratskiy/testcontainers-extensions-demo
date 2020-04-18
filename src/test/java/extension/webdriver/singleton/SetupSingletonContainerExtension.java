package extension.webdriver.singleton;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.DefaultRecordingFileFactory;
import org.testcontainers.lifecycle.TestDescription;

import java.io.File;

import static driver.options.CapabilitiesFactory.getDriverCapabilities;
import static java.time.Duration.ofMinutes;
import static org.apache.commons.io.FileUtils.ONE_MB;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

public class SetupSingletonContainerExtension implements BeforeAllCallback, AfterAllCallback, AfterEachCallback {
    private static final String CONTAINER_KEY = "single.testcontainer";
    private static BrowserWebDriverContainer container;

    @Override
    public void beforeAll(ExtensionContext context) {
        container = new BrowserWebDriverContainer<>();
        container.withCapabilities(getDriverCapabilities());
        container.withRecordingMode(RECORD_ALL, new File("build"));
        container.withRecordingFileFactory(new DefaultRecordingFileFactory());
        container.withStartupAttempts(3);
        container.withStartupTimeout(ofMinutes(5));
        container.withSharedMemorySize(1024L * ONE_MB);
        container.setPrivilegedMode(true);
        container.start();
        context.getStore(GLOBAL).put(CONTAINER_KEY, container);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        container.afterTest(
                new TestDescription() {
                    @Override
                    public String getTestId() {
                        return context.getUniqueId();
                    }

                    @Override
                    public String getFilesystemFriendlyName() {
                        return context.getDisplayName();
                    }
                }, context.getExecutionException()
        );
    }

    @Override
    public void afterAll(ExtensionContext context) {
        container.stop();
        context.getStore(GLOBAL).remove(CONTAINER_KEY);
    }
}
