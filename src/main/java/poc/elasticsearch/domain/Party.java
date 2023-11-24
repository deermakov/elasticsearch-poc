package poc.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(Individual.class),
    @JsonSubTypes.Type(IndividualEntrepreneur.class),
    @JsonSubTypes.Type(LegalEntity.class)
})
@Document("party")
@Data
@ToString
public abstract class Party {
    @Id
    private String id;// автогенерация из коробки поддерживается для типов String, BigInteger, ObjectId
    private String inn;
    private Address address;

    /*
        Сейчас ссылки на participants сохраняются в Deal (см. DealEventProcessor),
            это можно видеть в БД (через Monge Express),
            а в Party ссылка на deals не сохраняется (автоматом это не происходит).
            Поэтому чтобы при считывании Party вычитать её deals,
            применяем здесь lookup (в формате "поле в Deal" : "поле в Party").
     */
    @DocumentReference(
        collection = "deal",
        lookup = "{'participants':?#{#self._id} }",
        lazy = true
    )
    private List<Deal> deals;
}
