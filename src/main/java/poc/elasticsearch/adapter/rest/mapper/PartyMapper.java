package poc.elasticsearch.adapter.rest.mapper;

import org.mapstruct.Mapper;
import poc.elasticsearch.adapter.rest.dto.IndividualDto;
import poc.elasticsearch.adapter.rest.dto.LegalEntityDto;
import poc.elasticsearch.domain.Individual;
import poc.elasticsearch.domain.LegalEntity;

@Mapper(componentModel = "spring")
public interface PartyMapper {

    LegalEntityDto leToDto(LegalEntity legalEntity);

    IndividualDto indToDto(Individual individual);
}
