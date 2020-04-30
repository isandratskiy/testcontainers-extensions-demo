package extension.compose;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

@SeleniumCompose
public class SeleniumDockerComposeTest {

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
