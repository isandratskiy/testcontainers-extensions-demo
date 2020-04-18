package extension.webdriver.singleton;

import extension.webdriver.resolver.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

@SetupSingletonContainer
public class BaseSingletonTest {

    @BeforeAll
    static void setup(@Inject RemoteWebDriver driver) {
        setWebDriver(driver);
        baseUrl = "https://the-internet.herokuapp.com";
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
}