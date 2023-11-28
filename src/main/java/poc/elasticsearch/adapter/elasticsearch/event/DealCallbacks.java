package poc.elasticsearch.adapter.elasticsearch.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.core.event.AfterSaveCallback;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import poc.elasticsearch.adapter.elasticsearch.repository.PartyRepository;
import poc.elasticsearch.domain.Deal;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DealCallbacks implements AfterSaveCallback<Deal> {
    // Такое внедрение из-за циклической зависимости
    @Autowired
    @Lazy
    private PartyRepository partyRepository;

    /**
     * Дополнительно отстреливает Deal.participants в индекс для Party, чтобы потом по ним искать
     */
    @Override
    public Deal onAfterSave(Deal entity, IndexCoordinates index) {
        Optional.ofNullable(entity.getParticipants())
            .ifPresent(partyRepository::saveAll);
        return entity;
    }
}
