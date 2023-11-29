package poc.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;

@Document(indexName = "idx_deal")
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) // для красоты
public class Deal {
    @Id
    private String id;
    private String number;
    private BigDecimal amount;

    @Field(type = FieldType.Nested)
    private List<Party> participants;
}
