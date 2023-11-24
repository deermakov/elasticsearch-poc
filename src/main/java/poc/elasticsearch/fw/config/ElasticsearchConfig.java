package poc.elasticsearch.fw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories("poc.elasticsearch.adapter.elasticsearch.repository")
public class ElasticsearchConfig {
}
