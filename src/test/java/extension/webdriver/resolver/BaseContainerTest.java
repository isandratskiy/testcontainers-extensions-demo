package extension.webdriver.resolver;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;

import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.*;
import static java.lang.System.*;
import static java.time.Duration.ofMinutes;
import static org.apache.commons.io.FileUtils.ONE_MB;
import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.*;

@SetupBrowserContainer
public class BaseContainerTest {

    @Inject
    @Container
    private BrowserWebDriverContainer container;

    @BeforeEach
    void setup() {
        this.container.withStartupAttempts(2);
        this.container.withStartupTimeout(ofMinutes(5));
        this.container.withSharedMemorySize(1024L * ONE_MB);
        this.container.setPrivilegedMode(true);
        this.container.start();
        setWebDriver(this.container.getWebDriver());
        baseUrl = "https://the-internet.herokuapp.com";
        out.println("Container ID : " + this.container.getContainerId());
    }

    @Test
    void canPassForgotPassword() {
        open("/forgot_password");
        $("#email").val(randomAlphabetic(5) + "@icloude.com");
        $("#form_submit").click();
        assertEquals("Your e-mail's been sent!", $("#content").text().trim());
    }

    @Test
    void canLoginWithCredentials() {
        open("/login");
        $("#username").val("tomsmith");
        $("#password").val("SuperSecretPassword!");
        $(".radius").click();
        $(".flash success");
        assertTrue($(".flash").text().contains("You logged into a secure area"));
    }

    @AfterEach
    void shutdown() {
        getWebDriver().quit();
        this.container.stop();
    }
}
