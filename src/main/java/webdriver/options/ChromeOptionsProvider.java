package webdriver.options;

import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

import static java.util.List.of;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class ChromeOptionsProvider {
    public static ChromeOptions getOptions() {
        var options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.setExperimentalOption("excludeSwitches", of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("prefs", disablePasswordManager());
        options.setAcceptInsecureCerts(true);
        options.setHeadless(false);
        return options;
    }

    private static Map<String, Boolean> disablePasswordManager() {
        return ofEntries(
                entry("credentials_enable_service", false),
                entry("profile.password_manager_enabled", false));
    }
}
