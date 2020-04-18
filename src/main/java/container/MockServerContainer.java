package container;

import org.testcontainers.containers.GenericContainer;

import static java.lang.String.*;

public class MockServerContainer extends GenericContainer<MockServerContainer> {
    public static final String VERSION = "5.10.0";
    public static final int PORT = 1080;

    public MockServerContainer() {
        this(VERSION);
    }

    public MockServerContainer(String version) {
        super("jamesdbloom/mockserver:mockserver-" + version);
        withCommand("-logLevel INFO -serverPort " + PORT);
        addExposedPorts(PORT);
    }

    public String getHost() {
        return format("http://%s:%d", getContainerIpAddress(), getMappedPort(PORT));
    }

    public Integer getPort() {
        return getMappedPort(PORT);
    }
}
