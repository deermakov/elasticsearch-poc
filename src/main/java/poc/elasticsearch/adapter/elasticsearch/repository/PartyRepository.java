package poc.elasticsearch.adapter.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import poc.elasticsearch.domain.Party;

public interface PartyRepository extends ElasticsearchRepository<Party, String> {
    //Optional<Party> update(@Param("id") String id, @Param("overridingParty") Party overridingParty);
}
