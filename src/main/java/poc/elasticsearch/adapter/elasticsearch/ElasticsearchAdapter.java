package poc.elasticsearch.adapter.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;
import poc.elasticsearch.adapter.elasticsearch.repository.DealRepository;
import poc.elasticsearch.adapter.elasticsearch.repository.PartyRepository;
import poc.elasticsearch.app.api.Storage;
import poc.elasticsearch.domain.Deal;
import poc.elasticsearch.domain.Party;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class ElasticsearchAdapter implements Storage {
    private final PartyRepository partyRepository;
    private final DealRepository dealRepository;

    @Override
    public void saveParty(Party party) {
        partyRepository.save(party);
    }

    @Override
    public List<Party> getAllParties() {

        Stream<Party> dealParticipants = dealRepository.findAllParticipants()
            .flatMap(deal -> deal.getParticipants().stream())
            .stream();

        Stream<Party> freeParties = partyRepository.findAll().stream();

        // todo заменить на Multisearch
        return Stream.concat(dealParticipants, freeParties).toList();
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
        return StreamSupport.stream(dealRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }
}
