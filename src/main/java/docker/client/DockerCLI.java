package docker.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Container;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.List;

import static com.github.dockerjava.core.DockerClientBuilder.*;
import static java.lang.String.*;
import static java.util.Arrays.*;

public class DockerCLI {

    private final DockerClient client;

    public DockerCLI() {
        this.client = getInstance().build();
    }

    public List<Container> getContainers() {
        return this.client.listContainersCmd()
                .withStatusFilter(asList("running"))
                .exec();
    }

    public Container getContainerByName(String name) {
        return this.getContainers().stream()
                .filter(container -> container.getNames()[0].contains(name))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found container by name: " + name));
    }

    public boolean isRunning(String name) {
        return this.getContainerByName(name).getState().equals("running");
    }

    public boolean warmingUp(String containerName, Duration timer) {
        return this.getContainerByName(containerName).getStatus().contains(valueOf(timer.getSeconds()));
    }

    @SneakyThrows
    public void close() {
        this.client.close();
    }
}
