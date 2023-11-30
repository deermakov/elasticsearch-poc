package poc.elasticsearch.app.impl.party;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import poc.elasticsearch.app.api.Storage;
import poc.elasticsearch.domain.Party;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetPartyUseCase {
    private final Storage storage;

    public List<Party> execute(String text) {
        return storage.getParty(text);
    }
}
