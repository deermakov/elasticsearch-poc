package poc.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) // для красоты
public class IndividualEntrepreneur extends LegalEntity {

    @DocumentReference
    //@DBRef
    private Individual individual;// Individual is referenced entity !
    private Boolean selfEmployed;
}
