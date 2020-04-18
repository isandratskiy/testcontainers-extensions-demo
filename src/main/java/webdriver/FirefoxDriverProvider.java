package webdriver;

import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static webdriver.options.FirefoxOptionsProvider.getOptions;
import static io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver;

public class FirefoxDriverProvider implements WebDriverProvider {
    private static String instance;

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        firefoxdriver().setup();
        return new FirefoxDriver(getOptions());
    }
}
