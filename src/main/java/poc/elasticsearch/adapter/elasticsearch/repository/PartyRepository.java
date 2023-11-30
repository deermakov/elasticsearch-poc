package poc.elasticsearch.adapter.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.util.Streamable;
import poc.elasticsearch.domain.Party;

public interface PartyRepository extends ElasticsearchRepository<Party, String> {
    Streamable<Party> findAll();

    //Optional<Party> update(@Param("id") String id, @Param("overridingParty") Party overridingParty);
}
