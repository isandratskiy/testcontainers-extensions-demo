package webdriver;

import lombok.SneakyThrows;

import java.net.URL;

import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static webdriver.WebDriverFactory.Browser.*;
import static java.lang.System.getProperty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class WebDriverFactory {
    private static final String BROWSER_PROPERTY = "browser";

    public static void createDriverInstance() {
        switch (getBrowserProperty()) {
            case "chrome":
                REMOTE_CHROME.start();
                break;
            case "firefox":
                REMOTE_FIREFOX.start();
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
            void start() {
                var clazz = ChromeDriverProvider.class;
                setRemoteInstance(clazz);
                browser = clazz.getName();
            }
        },
        REMOTE_FIREFOX {
            @Override
            void start() {
                var clazz = FirefoxDriverProvider.class;
                setRemoteInstance(clazz);
                browser = clazz.getName();
            }
        };

        abstract void start();
    }

    private static String getBrowserProperty() {
        return getProperty(BROWSER_PROPERTY) == null
                ? EMPTY
                : getProperty(BROWSER_PROPERTY);
    }

    @SneakyThrows
    private static void setRemoteInstance(Class clazz) {
        var field = clazz.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, new URL("http://0.0.0.0:4444/wd/hub"));
    }
}
