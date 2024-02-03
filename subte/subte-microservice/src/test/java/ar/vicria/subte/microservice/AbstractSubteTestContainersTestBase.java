package ar.vicria.subte.microservice;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Path;

/**
 * Base class for Subte-related integration tests using TestContainers.
 */
public abstract class AbstractSubteTestContainersTestBase {
    public static Network network = Network.newNetwork();

    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withNetwork(network)
            .withNetworkAliases("postgres")
            .withDatabaseName("subte")
            .withUsername("subte0us3r")
            .withPassword("passw0rdsubt3");

    public static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
            .withNetwork(network)
            .withNetworkAliases("kafka");

    public static GenericContainer<?> subteContainer = new GenericContainer<>(new ImageFromDockerfile().withDockerfile(Path.of("./Dockerfile")))
            .withNetwork(network)
            .withEnv("spring.datasource.url", "jdbc:postgresql://postgres:5432/subte")
            .withExposedPorts(8082);

    @BeforeAll
    public static void startContainers() {
        postgreSQLContainer.start();
        kafkaContainer.start();
        subteContainer.start();
    }

    @AfterAll
    public static void stopContainers() {
        subteContainer.stop();
        postgreSQLContainer.stop();
        kafkaContainer.stop();
    }
}