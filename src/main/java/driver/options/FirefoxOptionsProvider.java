package driver.options;

import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxOptionsProvider {
    public static FirefoxOptions getOptions() {
        var options = new FirefoxOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-web-security");
        options.setAcceptInsecureCerts(true);
        options.setHeadless(false);
        return options;
    }
}
