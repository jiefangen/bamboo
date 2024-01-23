package org.panda.tech.data.mongo.config;

import org.panda.tech.data.mongo.support.MongoAccessTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * MongoDB数据层配置
 */
@Configuration
public class MongoDataConfiguration {

    protected String getSchema() {
        return null;
    }

    @Bean
    public MongoAccessTemplate mongoAccessTemplate(MongoTemplate mongoTemplate) {
        String schema = getSchema();
        if (schema == null) {
            return new MongoAccessTemplate(mongoTemplate);
        } else {
            return new MongoAccessTemplate(schema, mongoTemplate);
        }
    }

}
