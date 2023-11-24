package poc.elasticsearch.adapter.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import poc.elasticsearch.adapter.elasticsearch.repository.DealRepository;
import poc.elasticsearch.adapter.elasticsearch.repository.PartyRepository;
import poc.elasticsearch.app.api.Storage;
import poc.elasticsearch.domain.Deal;
import poc.elasticsearch.domain.Party;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MongoDbAdapter implements Storage {
    private final PartyRepository partyRepository;
    private final DealRepository dealRepository;

    @Override
    public void saveParty(Party party) {
        partyRepository.save(party);
    }

    @Override
    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }

    @Override
    public Optional<Party> getParty(String id) {
        return partyRepository.findById(id);
    }

    @Override
    public void saveDeal(Deal deal) {
        dealRepository.save(deal);
    }

    @Override
    public List<Deal> getAllDeals() {
        List<Deal> list = dealRepository.findAll();
        list.forEach(deal -> deal.getParticipants().forEach(
                party -> party.setDeals(null)
            )
        );
        return list;
    }
}
