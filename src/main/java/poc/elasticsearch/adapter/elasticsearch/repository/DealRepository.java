package poc.elasticsearch.adapter.elasticsearch.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.annotations.SourceFilters;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.util.Streamable;
import poc.elasticsearch.domain.Deal;
import poc.elasticsearch.domain.Party;

import java.util.List;

public interface DealRepository extends ElasticsearchRepository<Deal, String> {
    @Query(
    """
    {
        "nested": {
            "path": "participants",
            "query": {
                "match_all": {}
            }
        }
    }
    """
    )
    @SourceFilters(includes = "participants")
    /* Кастомный метод для выборки всех сделок.
    Можно было бы использовать просто findAll(), но из все сделки нам нужны только
    participants, поэтому хочется использовать @SourceFilters, и при этом не хочется портить
    findAll() этой аннотацией */
    Streamable<Deal> findAllParticipants();

    Streamable<Deal> findByParticipants_Inn(String inn);
}
