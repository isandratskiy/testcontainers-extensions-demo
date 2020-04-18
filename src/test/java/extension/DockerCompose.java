package extension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
public class DockerCompose {

    @Container
    private DockerComposeContainer composeContainer = new DockerComposeContainer(
            new File("docker-compose.yaml"))
            .waitingFor("selenium-hub", Wait.defaultWaitStrategy());

    @BeforeEach
    void setup() {
        System.out.println(composeContainer.getServiceHost("selenium-hub", 4445));
    }

    @Test
    void test() {
        System.out.println(composeContainer.getServiceHost("selenium-hub", 4445));
    }
}
