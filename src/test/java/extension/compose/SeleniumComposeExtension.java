package extension.compose;

import org.junit.jupiter.api.extension.*;
import webdriver.compose.SeleniumComposeContainer;

import static com.codeborne.selenide.Configuration.baseUrl;
import static webdriver.WebDriverFactory.*;

public class SeleniumComposeExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {
    private static final String ENVIRONMENT_KEY = "compose.key";

    private static SeleniumComposeContainer environment;

    @Override
    public void beforeAll(ExtensionContext context) {
        environment = new SeleniumComposeContainer();
        environment.start();
        environment.isChromeRunning();

        baseUrl = "https://the-internet.herokuapp.com";
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        var instance = environment.getInstanceUrl();
        createDriverInstance(instance);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        shutdownDriverInstance();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        environment.stop();
    }
}
