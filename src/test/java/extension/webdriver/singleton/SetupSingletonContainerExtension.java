package extension.webdriver.singleton;

import extension.webdriver.resolver.Inject;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.DefaultRecordingFileFactory;
import org.testcontainers.lifecycle.TestDescription;

import java.io.File;

import static webdriver.options.CapabilitiesFactory.getDriverCapabilities;
import static java.time.Duration.ofMinutes;
import static org.apache.commons.io.FileUtils.ONE_MB;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

public class SetupSingletonContainerExtension implements BeforeAllCallback, AfterAllCallback, AfterEachCallback, ParameterResolver {
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

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(RemoteWebDriver.class);
    }

    @Override
    public RemoteWebDriver resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var container = extensionContext.getStore(GLOBAL).get(CONTAINER_KEY, BrowserWebDriverContainer.class);
        if (parameterContext.isAnnotated(Inject.class))
            return container.getWebDriver();
        else throw new ParameterResolutionException("Not found injectable Container.class field");
    }
}
