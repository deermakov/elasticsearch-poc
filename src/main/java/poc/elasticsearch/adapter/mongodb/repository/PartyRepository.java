package poc.elasticsearch.adapter.mongodb.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import poc.elasticsearch.domain.Party;

import java.util.Optional;

public interface PartyRepository extends MongoRepository<Party, String> {
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
    Optional<Party> update(@Param("id") String id, @Param("overridingParty")Party overridingParty);
}
