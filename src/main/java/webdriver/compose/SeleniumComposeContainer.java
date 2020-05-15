package webdriver.compose;

import docker.client.DockerCLI;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

import static java.lang.String.format;
import static java.time.Duration.*;
import static org.awaitility.Awaitility.await;

public class SeleniumComposeContainer extends DockerComposeContainer<SeleniumComposeContainer> {
    private static final String COMPOSE_NAME = "docker-compose.yaml";
    private static final String SERVICE_NAME = "selenium-hub";
    private static final String CHROME_NAME = "chrome";
    private static final String FIREFOX_NAME = "firefox";
    private static final int SERVICE_PORT = 4444;

    private DockerCLI client;

    public SeleniumComposeContainer() {
        this(COMPOSE_NAME);
        this.withLocalCompose(true);
        this.client = new DockerCLI();
    }

    public SeleniumComposeContainer(String composeName) {
        super(new File(composeName));
    }

    public String getInstanceUrl() {
        return format("http://0.0.0.0:%d/wd/hub", SERVICE_PORT);
    }

    public void isHubRunning() {
        await().until(() -> client.warmingUp(SERVICE_NAME, ofSeconds(5)));
    }
}
