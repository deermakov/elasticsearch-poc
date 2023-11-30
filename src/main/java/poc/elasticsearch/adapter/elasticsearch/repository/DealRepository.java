package poc.elasticsearch.adapter.elasticsearch.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.annotations.SourceFilters;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.util.Streamable;
import poc.elasticsearch.domain.Deal;
import poc.elasticsearch.domain.Party;

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
       - Можно было бы использовать просто findAll(), но из все сделки нам нужны только
        participants, поэтому хочется использовать @SourceFilters, и при этом не хочется
        портить findAll() этой аннотацией.
       - Nested query здесь тоже функционально не нужен, т.к. в запросе нет параметров на
        participant'ов. Сделано просто для примера. */
    Streamable<Deal> findAllParticipants();

    @Query(
        """
            {
                "multi_match" : {
                  "query":      "?0"
                }
            }
            """
    )
    Streamable<Deal> findByText(String text);

}
