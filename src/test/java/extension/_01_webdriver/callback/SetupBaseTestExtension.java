package extension._01_webdriver.callback;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.DefaultRecordingFileFactory;
import org.testcontainers.lifecycle.TestDescription;

import java.io.File;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static http.Logger.logInfo;
import static java.time.Duration.ofMinutes;
import static org.apache.commons.io.FileUtils.ONE_MB;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;
import static webdriver.options.CapabilitiesFactory.getDriverCapabilities;

public class SetupBaseTestExtension implements BeforeEachCallback, AfterEachCallback, BeforeAllCallback {
    private static final String CONTAINER_KEY = "callback.testcontainer.setup";

    @Override
    public void beforeAll(ExtensionContext context) {
        baseUrl = "https://the-internet.herokuapp.com";
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        var container = new BrowserWebDriverContainer<>();
        container.withCapabilities(getDriverCapabilities());
        container.withRecordingMode(RECORD_ALL, new File("build"));
        container.withRecordingFileFactory(new DefaultRecordingFileFactory());
        container.withStartupAttempts(3);
        container.withStartupTimeout(ofMinutes(5));
        container.withSharedMemorySize(1024L * ONE_MB);
        container.withPrivilegedMode(true);
        container.start();

        setWebDriver(container.getWebDriver());

        context.getStore(GLOBAL).put(CONTAINER_KEY, container);
        logInfo("Container ID : " + container.getContainerId());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        var container = context.getStore(GLOBAL).get(CONTAINER_KEY, BrowserWebDriverContainer.class);
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
