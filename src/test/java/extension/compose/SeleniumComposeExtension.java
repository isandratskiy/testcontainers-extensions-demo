package extension.compose;

import org.junit.jupiter.api.extension.*;
import webdriver.compose.SeleniumComposeContainer;

import static com.codeborne.selenide.Configuration.baseUrl;
import static webdriver.WebDriverFactory.*;

public class SeleniumComposeExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {
    private static final String ENVIRONMENT_KEY = "compose.key";

    private static final SeleniumComposeContainer ENVIRONMENT = new SeleniumComposeContainer();
    private static String environmentInstance;

    @Override
    public void beforeAll(ExtensionContext context) {
        ENVIRONMENT.start();
        environmentInstance = ENVIRONMENT.getInstanceUrl();
        baseUrl = "https://the-internet.herokuapp.com";
        checkInstanceState(environmentInstance);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        createDriverInstance(environmentInstance);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        shutdownDriverInstance();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        ENVIRONMENT.stop();
    }
}
