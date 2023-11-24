package poc.elasticsearch.adapter.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import poc.elasticsearch.domain.Deal;

public interface DealRepository extends ElasticsearchRepository<Deal, String> {
}
