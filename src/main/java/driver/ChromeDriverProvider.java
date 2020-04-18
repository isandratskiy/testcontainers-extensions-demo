package driver;

import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static driver.options.ChromeOptionsProvider.getOptions;
import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;

public class ChromeDriverProvider implements WebDriverProvider {
    private static String instance;

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        chromedriver().setup();
        return new ChromeDriver(getOptions());
    }
}
