package poc.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) // для красоты
public class IndividualEntrepreneur extends LegalEntity {
    //@Field(type = FieldType.Nested) - почему-то не работает, поле создается не-nested
    private Individual individual;
    private Boolean selfEmployed;
}
