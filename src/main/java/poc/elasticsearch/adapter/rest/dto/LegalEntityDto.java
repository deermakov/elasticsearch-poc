package poc.elasticsearch.adapter.rest.dto;

import lombok.Data;

@Data
public class LegalEntityDto extends PartyDto {
    private String name;
}
