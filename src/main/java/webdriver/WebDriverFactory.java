package webdriver;

import lombok.SneakyThrows;

import java.net.URL;

import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static webdriver.WebDriverFactory.Browser.*;
import static java.lang.System.getProperty;

public final class WebDriverFactory {
    private static final String BROWSER_PROPERTY = "browser";

    private WebDriverFactory() {
    }

    public static void createDriverInstance(String instance) {
        switch (getBrowserProperty()) {
            case "chrome":
                REMOTE_CHROME.start(instance);
                break;
            case "firefox":
                REMOTE_FIREFOX.start(instance);
                break;
            default: throw new IllegalStateException(
                    "Wrong browser type " + getBrowserProperty()
            );
        }
    }

    public static void shutdownDriverInstance() {
        getWebDriver().quit();
    }

    enum Browser {
        REMOTE_CHROME {
            @Override
            void start(String instance) {
                var clazz = ChromeDriverProvider.Remote.class;
                setRemoteInstance(clazz, instance);
                browser = clazz.getName();
            }
        },
        REMOTE_FIREFOX {
            @Override
            void start(String instance) {
                var clazz = FirefoxDriverProvider.Remote.class;
                setRemoteInstance(clazz, instance);
                browser = clazz.getName();
            }
        };

        abstract void start(String instance);
    }

    private static String getBrowserProperty() {
        return getProperty(BROWSER_PROPERTY, "chrome");
    }

    @SneakyThrows
    private static void setRemoteInstance(Class<?> clazz, String instance) {
        var field = clazz.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, new URL(instance));
    }
}
