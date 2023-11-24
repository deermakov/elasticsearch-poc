package poc.elasticsearch.app.impl.party;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import poc.elasticsearch.app.api.Storage;
import poc.elasticsearch.domain.Party;

@Component
@RequiredArgsConstructor
public class SavePartyUseCase {
    private final Storage storage;

    public void execute(Party party) {
        storage.saveParty(party);
    }
}
