package org.panda.tech.data.mongo.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Collections;

@AutoConfigureBefore(MongoDataAutoConfiguration.class)
public abstract class EmbeddedMongoConfigurerSupport {

    @Bean(destroyMethod = "shutdown")
    public MongoServer mongoServer() {
        MongoServer server = new MongoServer(new MemoryBackend());
        server.bind();
        return server;
    }

    @Bean(destroyMethod = "close")
    public MongoClient mongoClient(MongoServer mongoServer) {
        MongoClientSettings settings = MongoClientSettings.builder().applyToClusterSettings(clusterSettings -> {
            clusterSettings.hosts(Collections.singletonList(new ServerAddress(mongoServer.getLocalAddress())));
        }).build();
        MongoDriverInformation driver = MongoDriverInformation.builder().build();
        return new MongoClientImpl(settings, driver);
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, "test");
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }

}
