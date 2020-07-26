package extension.webdriver.concurrent;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.DefaultRecordingFileFactory;
import org.testcontainers.lifecycle.TestDescription;

import java.io.File;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static http.Logger.logInfo;
import static java.time.Duration.ofMinutes;
import static org.apache.commons.io.FileUtils.ONE_MB;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;
import static webdriver.options.CapabilitiesFactory.getDriverCapabilities;

public class SetupBaseTestExtension implements BeforeEachCallback, AfterEachCallback{
    private static final String CONTAINER_KEY = "each.testcontainer";

    @Override
    public void beforeEach(ExtensionContext context) {
        var container = new BrowserWebDriverContainer<>();
        container.withCapabilities(getDriverCapabilities());
        container.withRecordingMode(RECORD_ALL, new File("build"));
        container.withRecordingFileFactory(new DefaultRecordingFileFactory());
        container.withStartupAttempts(3);
        container.withStartupTimeout(ofMinutes(5));
        container.withSharedMemorySize(1024L * ONE_MB);
        container.setPrivilegedMode(true);
        container.start();

        setWebDriver(container.getWebDriver());

        context.getStore(GLOBAL).put(CONTAINER_KEY, container);
        logInfo("Container ID : " + container.getContainerId());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        var container = context.getStore(GLOBAL).get(CONTAINER_KEY, BrowserWebDriverContainer.class);
        getWebDriver().quit();
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
        context.getStore(GLOBAL).remove(CONTAINER_KEY);
    }
}
