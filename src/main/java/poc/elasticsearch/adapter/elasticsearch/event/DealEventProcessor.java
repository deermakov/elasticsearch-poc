package poc.elasticsearch.adapter.elasticsearch.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import poc.elasticsearch.adapter.elasticsearch.repository.PartyRepository;
import poc.elasticsearch.domain.Deal;
import poc.elasticsearch.domain.Party;

@Component
@RequiredArgsConstructor
@Slf4j
public class DealEventProcessor extends AbstractMongoEventListener<Deal> {
    private final PartyRepository partyRepository;

    /**
     * Spring Data MongoDB doesn't support cascade save,
     * so we have to perform it explicitly
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Deal> event) {
        Deal deal = event.getSource();
        updateParticipants(deal);// производительность (кол-во read/save) здесь не учитываем
    }

    /**
     * Пример обновления существующего ФЛ с помощью aggregation pipeline
     */
    private void updateParticipants(Deal deal) {
        for (Party party : deal.getParticipants()) {
            Party updatedParty = party;
            if (party.getId() != null) {
                updatedParty = partyRepository
                    .update(party.getId(), party)
                    .orElse(party);
            }
            updatedParty.setDeals(null);// иначе будет ошибка при сохранении, и вообще, не храним deals в party
            partyRepository.save(updatedParty);
        }
    }
}
