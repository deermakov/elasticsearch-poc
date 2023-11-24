package poc.elasticsearch.app.impl.deal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import poc.elasticsearch.app.api.Storage;
import poc.elasticsearch.domain.Deal;

@Component
@RequiredArgsConstructor
public class SaveDealUseCase {
    private final Storage storage;

    public void execute(Deal deal) {
        storage.saveDeal(deal);
    }
}
