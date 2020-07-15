package mock.container;

import org.testcontainers.containers.GenericContainer;

import static java.lang.String.*;

public class MockServerContainer extends GenericContainer<MockServerContainer> {
    public static final String VERSION = "5.10.0";
    public static final int PORT = 1080;

    private static final String CONTAINER_NAME = "jamesdbloom/mockserver:mockserver-";

    public MockServerContainer() {
        this(VERSION);
    }

    public MockServerContainer(String version) {
        super(CONTAINER_NAME + version);
        super.withExposedPorts(PORT);
    }

    public String getHost() {
        return format("http://%s:%d", super.getContainerIpAddress(), this.getPort());
    }

    public Integer getPort() {
        return getMappedPort(PORT);
    }
}