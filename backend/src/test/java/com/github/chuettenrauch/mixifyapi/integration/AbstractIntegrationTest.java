package com.github.chuettenrauch.mixifyapi.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class AbstractIntegrationTest {
    private static final int MONGO_PORT = 27017;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final DockerImageName mongoImage = DockerImageName.parse("mongo:5.0");

    @Container
    private static final MongoDBContainer mongoContainer = new MongoDBContainer(mongoImage)
            .withExposedPorts(MONGO_PORT)
            .withReuse(true);

    @DynamicPropertySource
    static void registerMongoUri(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> String.format(
                "mongodb://%s:%s",
                mongoContainer.getHost(),
                mongoContainer.getMappedPort(MONGO_PORT))
        );
    }

    @BeforeEach
    void clearMongo() {
        this.mongoTemplate.getDb().drop();
    }


}
