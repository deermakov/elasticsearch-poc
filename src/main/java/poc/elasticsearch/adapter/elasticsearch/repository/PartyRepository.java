package poc.elasticsearch.adapter.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import poc.elasticsearch.domain.Party;

import java.util.Optional;

public interface PartyRepository extends ElasticsearchRepository<Party, String> {
    // merge объектов с помощью aggregation pipeline
    @Aggregation({
        """
                {
                    $match: {"_id": ?0}
                }
            """,
        """
                {                 
                    $replaceWith: {
                        $mergeObjects: [$$CURRENT, ?1]
                    }
                }
            """
    })
    Optional<Party> update(@Param("id") String id, @Param("overridingParty") Party overridingParty);
}
