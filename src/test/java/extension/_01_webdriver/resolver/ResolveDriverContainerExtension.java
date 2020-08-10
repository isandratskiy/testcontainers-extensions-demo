package extension._01_webdriver.resolver;

import extension._01_webdriver.injector.Inject;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.DefaultRecordingFileFactory;
import org.testcontainers.lifecycle.TestDescription;

import java.io.File;

import static extension.Namespace.RESOLVER_CONTAINER;
import static http.Logger.logInfo;
import static java.time.Duration.ofMinutes;
import static org.apache.commons.io.FileUtils.ONE_MB;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;
import static webdriver.options.CapabilitiesFactory.getDriverCapabilities;

public class ResolveDriverContainerExtension implements BeforeAllCallback, AfterAllCallback, AfterEachCallback, ParameterResolver {
    private static final Namespace NAMESPACE = create(RESOLVER_CONTAINER);
    private String CONTAINER_KEY;

    @Override
    public void beforeAll(ExtensionContext context) {
        var container = new BrowserWebDriverContainer<>();
        container.withCapabilities(getDriverCapabilities());
        container.withRecordingMode(RECORD_ALL, new File("build"));
        container.withRecordingFileFactory(new DefaultRecordingFileFactory());
        container.withStartupAttempts(3);
        container.withStartupTimeout(ofMinutes(5));
        container.withSharedMemorySize(1024L * ONE_MB);
        container.withPrivilegedMode(true);
        container.start();

        this.CONTAINER_KEY = container.getContainerId();

        context.getStore(NAMESPACE).put(CONTAINER_KEY, container);
        logInfo("Container ID : " + container.getContainerId());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == RemoteWebDriver.class;
    }

    @Override
    public RemoteWebDriver resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        var container = extensionContext.getStore(NAMESPACE).get(CONTAINER_KEY, BrowserWebDriverContainer.class);
        if (parameterContext.isAnnotated(Inject.class))
            return container.getWebDriver();
        else throw new ParameterResolutionException("Not found injectable Container.class field");
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
                        return context.getDisplayName();
                    }
                },
                context.getExecutionException());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        var container = context.getStore(NAMESPACE).get(CONTAINER_KEY, BrowserWebDriverContainer.class);
        container.stop();
        context.getStore(GLOBAL).remove(CONTAINER_KEY);
    }
}
