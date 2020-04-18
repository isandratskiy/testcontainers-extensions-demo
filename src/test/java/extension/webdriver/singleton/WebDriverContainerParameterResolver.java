package extension.webdriver.singleton;

import extension.webdriver.resolver.Inject;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class WebDriverContainerParameterResolver implements ParameterResolver {
    private static final String CONTAINER_KEY = "single.testcontainer";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(RemoteWebDriver.class);
    }

    @Override
    public RemoteWebDriver resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var container = extensionContext.getStore(GLOBAL).get(CONTAINER_KEY, BrowserWebDriverContainer.class);
        if (parameterContext.isAnnotated(Inject.class))
            return container.getWebDriver();
        else throw new ParameterResolutionException("Not found injectable Container.class field");
    }
}
