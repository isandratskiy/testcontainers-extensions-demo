package extension._01_webdriver.resolver;

import extension._01_webdriver.injector.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.matchesText;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

@ResolveDriverArgument
public class ResolveExampleTest {

    @BeforeAll
    static void setup(@Inject RemoteWebDriver driver) {
        baseUrl = "https://the-internet.herokuapp.com";
        setWebDriver(driver);
    }

    @Test
    void canPassForgotPassword() {
        open("/forgot_password");
        $("#email").val(randomAlphabetic(5) + "@icloude.com");
        $("#form_submit").click();
        $("#content").shouldHave(exactText("Your e-mail's been sent!"));
    }

    @Test
    void canLoginWithCredentials() {
        open("/login");
        $("#username").val("tomsmith");
        $("#password").val("SuperSecretPassword!");
        $(".radius").click();
        $(".flash").shouldHave(matchesText("You logged into a secure area!"));
    }
}
