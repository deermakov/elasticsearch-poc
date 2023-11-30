package poc.elasticsearch.app.api;

import poc.elasticsearch.domain.Deal;
import poc.elasticsearch.domain.Party;

import java.util.List;

public interface Storage {
    void saveParty(Party party);

    List<Party> getAllParties();

    List<Party> getParty(String text);

    List<Deal> getDeal(String text);

    void saveDeal(Deal deal);

    List<Deal> getAllDeals();
}
