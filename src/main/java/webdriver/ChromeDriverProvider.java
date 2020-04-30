package webdriver;

import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static webdriver.options.ChromeOptionsProvider.getOptions;

public class ChromeDriverProvider {

    static class Remote implements WebDriverProvider {
        private static URL instance;

        @Override
        public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
            return new RemoteWebDriver(instance, getOptions());
        }
    }
}
