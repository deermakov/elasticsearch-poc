package poc.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(Individual.class),
    @JsonSubTypes.Type(IndividualEntrepreneur.class),
    @JsonSubTypes.Type(LegalEntity.class)
})
@Document(indexName = "idx_party")
@Data
@ToString
public abstract class Party {
    @Id
    private String id;
    private String inn;
    private Address address;
    private List<Deal> deals;
    /*@JoinTypeRelations(
        relations =
            {
                @JoinTypeRelation(parent = "individualEntrepreneur", children = "individual")
            }
    )*/
    //private JoinField<String> relation;
}
