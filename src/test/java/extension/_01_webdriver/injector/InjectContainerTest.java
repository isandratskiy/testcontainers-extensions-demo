package extension._01_webdriver.injector;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.matchesText;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

@InjectBrowserContainer
public class InjectContainerTest {

    @Inject
    private BrowserWebDriverContainer browserContainer;

    @BeforeEach
    void setup() {
        baseUrl = "https://the-internet.herokuapp.com";
        setWebDriver(this.browserContainer.getWebDriver());
    }

    @SneakyThrows
    @Test
    void canPassForgotPassword() {
        open("/forgot_password");
        $("#email").val(randomAlphabetic(5) + "@icloude.com");
        $("#form_submit").click();
        $("#content").shouldHave(exactText("Your e-mail's been sent!"));
    }

    @SneakyThrows
    @Test
    void canLoginWithCredentials() {
        open("/login");
        $("#username").val("tomsmith");
        $("#password").val("SuperSecretPassword!");
        $(".radius").click();
        $(".flash").shouldHave(matchesText("You logged into a secure area!"));
    }
}
