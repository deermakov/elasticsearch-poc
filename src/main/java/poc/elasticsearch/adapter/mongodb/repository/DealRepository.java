package poc.elasticsearch.adapter.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import poc.elasticsearch.domain.Deal;

public interface DealRepository extends MongoRepository<Deal, String> {
}
