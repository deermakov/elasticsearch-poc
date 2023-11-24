package poc.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.math.BigDecimal;
import java.util.List;

@Document("deal")
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) // для красоты
public class Deal {
    @Id
    private String id;// автогенерация из коробки поддерживается для типов String, BigInteger, ObjectId
    private String number;
    private BigDecimal amount;
    @DocumentReference(collection = "party")
    private List<Party> participants;
}
