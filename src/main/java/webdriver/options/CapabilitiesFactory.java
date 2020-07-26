package webdriver.options;

import org.openqa.selenium.Capabilities;

import static java.lang.System.getProperty;

public final class CapabilitiesFactory {
    private static final String BROWSER_PROPERTY = "browser.container";

    private CapabilitiesFactory() {
    }

    public static Capabilities getDriverCapabilities() {
        switch (getBrowserProperty()) {
            case "chrome":
                return Option.CHROME.get();
            case "firefox":
                return Option.FIREFOX.get();
            default: throw new IllegalStateException("Incorrect browser type: " + getBrowserProperty());
        }
    }

    private static String getBrowserProperty() {
        return getProperty(BROWSER_PROPERTY, "chrome");
    }

    private enum Option {
        CHROME {
            @Override
            Capabilities get() {
                return ChromeOptionsProvider.getOptions();
            }
        },
        FIREFOX {
            @Override
            Capabilities get() {
                return FirefoxOptionsProvider.getOptions();
            }
        };

        abstract Capabilities get();
    }
}
