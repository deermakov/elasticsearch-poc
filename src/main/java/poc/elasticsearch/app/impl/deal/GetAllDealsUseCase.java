package poc.elasticsearch.app.impl.deal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import poc.elasticsearch.app.api.Storage;
import poc.elasticsearch.domain.Deal;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllDealsUseCase {
    private final Storage storage;

    public List<Deal> execute() {
        return storage.getAllDeals();
    }
}
