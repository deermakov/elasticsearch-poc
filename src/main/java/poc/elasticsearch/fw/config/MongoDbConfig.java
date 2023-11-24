package poc.elasticsearch.fw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("poc.elasticsearch.adapter.mongodb.repository")
public class MongoDbConfig {
}
