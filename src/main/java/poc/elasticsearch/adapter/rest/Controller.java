package poc.elasticsearch.adapter.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import poc.elasticsearch.app.impl.deal.GetAllDealsUseCase;
import poc.elasticsearch.app.impl.deal.SaveDealUseCase;
import poc.elasticsearch.app.impl.party.GetAllPartiesUseCase;
import poc.elasticsearch.app.impl.party.GetDealUseCase;
import poc.elasticsearch.app.impl.party.GetPartyUseCase;
import poc.elasticsearch.app.impl.party.SavePartyUseCase;
import poc.elasticsearch.domain.Deal;
import poc.elasticsearch.domain.Party;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final SavePartyUseCase savePartyUseCase;
    private final GetAllPartiesUseCase getAllPartiesUseCase;
    private final GetPartyUseCase getPartyUseCase;
    private final GetDealUseCase getDealUseCase;
    private final SaveDealUseCase saveDealUseCase;
    private final GetAllDealsUseCase getAllDealsUseCase;

    @PostMapping("/party/save")
    @Operation
    public void saveParty(@RequestBody Party party) {
        savePartyUseCase.execute(party);
    }

    @GetMapping("/party/list")
    public List<Party> getAllParties() {
        return getAllPartiesUseCase.execute();
    }

    @PostMapping("/party/search")
    public List<Party> getParty(@RequestParam String text) {
        return getPartyUseCase.execute(text);
    }

    @PostMapping("/deal/search")
    public List<Deal> getDeal(@RequestParam String text) {
        return getDealUseCase.execute(text);
    }

    @PostMapping("/deal/save")
    @Operation
    public void saveDeal(@RequestBody Deal deal) {
        saveDealUseCase.execute(deal);
    }

    @GetMapping("/deal/list")
    public List<Deal> getAllDeals() {
        return getAllDealsUseCase.execute();
    }
}
