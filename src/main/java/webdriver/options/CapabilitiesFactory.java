package webdriver.options;

import org.openqa.selenium.MutableCapabilities;

import static webdriver.options.CapabilitiesFactory.BrowserOption.*;
import static java.lang.System.getProperty;

public class CapabilitiesFactory {
    private static final String BROWSER_PROPERTY = "browser.container";

    public static MutableCapabilities getDriverCapabilities() {
        switch (getBrowserProperty()) {
            case "chrome":
                return CHROME.get();
            case "firefox":
                return FIREFOX.get();
            default: throw new IllegalStateException(
                    "Incorrect browser type: " + getBrowserProperty()
            );
        }
    }

    private static String getBrowserProperty() {
        return getProperty(BROWSER_PROPERTY, "chrome");
    }

    enum BrowserOption {
        CHROME {
            @Override
            MutableCapabilities get() {
                return ChromeOptionsProvider.getOptions();
            }
        },
        FIREFOX {
            @Override
            MutableCapabilities get() {
                return FirefoxOptionsProvider.getOptions();
            }
        };

        abstract MutableCapabilities get();
    }
}
