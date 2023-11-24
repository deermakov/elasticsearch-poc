package poc.elasticsearch.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import poc.elasticsearch.domain.Address;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@Data
public abstract class PartyDto {
    private String id;
    private String inn;
    private Address address;

    /*
    Если раскомментировать deals и перегенерить маппер (mvn clean compile),
    то в runtime это приведет к stack overflow в Jackson
     */
    //private List<Deal> deals;
}
