package webdriver.options;

import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;

import static java.util.Collections.*;

public class ChromeOptionsProvider {
    public static ChromeOptions getOptions() {
        var options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.setExperimentalOption("excludeSwitches", singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("prefs", disablePasswordManager());
        options.setAcceptInsecureCerts(true);
        options.setHeadless(false);
        return options;
    }

    private static HashMap<String, Boolean> disablePasswordManager() {
        var preference = new HashMap<String, Boolean>();
        preference.put("credentials_enable_service", false);
        preference.put("profile.password_manager_enabled", false);
        return preference;
    }
}
