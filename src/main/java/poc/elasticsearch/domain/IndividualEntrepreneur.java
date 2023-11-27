package poc.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) // для красоты
public class IndividualEntrepreneur extends LegalEntity {
    private Individual individual;
    private Boolean selfEmployed;
}
