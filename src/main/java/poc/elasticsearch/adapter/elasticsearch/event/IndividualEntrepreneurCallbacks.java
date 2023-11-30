package poc.elasticsearch.adapter.elasticsearch.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.core.event.AfterSaveCallback;
import org.springframework.data.elasticsearch.core.event.BeforeConvertCallback;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import poc.elasticsearch.adapter.elasticsearch.repository.PartyRepository;
import poc.elasticsearch.domain.Individual;
import poc.elasticsearch.domain.IndividualEntrepreneur;

@Component
@RequiredArgsConstructor
public class IndividualEntrepreneurCallbacks implements BeforeConvertCallback<IndividualEntrepreneur>, AfterSaveCallback<IndividualEntrepreneur> {
    // Такое внедрение из-за циклической зависимости
    @Autowired
    @Lazy
    private PartyRepository partyRepository;

    @Override
    public IndividualEntrepreneur onBeforeConvert(IndividualEntrepreneur entity, IndexCoordinates index) {
        if (entity.getIndividual() != null) {
            //           entity.setRelation(new JoinField<>("individualEntrepreneur"));
        }
        return entity;
    }

    /**
     * Дополнительно отстреливает IndividualEntrepreneur.individual в отдельный
     * документ типа Individual. Правда, они никак не будут связаны на уровне Elesticsearch
     */
    @Override
    public IndividualEntrepreneur onAfterSave(IndividualEntrepreneur entity, IndexCoordinates index) {
        Individual individual = entity.getIndividual();
        if (individual != null) {
            //          individual.setRelation(new JoinField<>("individual", entity.getId()));
            partyRepository.save(individual);
        }
        return entity;
    }
}
