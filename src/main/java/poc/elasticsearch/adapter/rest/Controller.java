package poc.elasticsearch.adapter.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import poc.elasticsearch.adapter.rest.dto.PartyDto;
import poc.elasticsearch.adapter.rest.mapper.PartyMapper;
import poc.elasticsearch.app.impl.deal.GetAllDealsUseCase;
import poc.elasticsearch.app.impl.deal.SaveDealUseCase;
import poc.elasticsearch.app.impl.party.GetAllPartiesUseCase;
import poc.elasticsearch.app.impl.party.SavePartyUseCase;
import poc.elasticsearch.domain.Deal;
import poc.elasticsearch.domain.Individual;
import poc.elasticsearch.domain.LegalEntity;
import poc.elasticsearch.domain.Party;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final SavePartyUseCase savePartyUseCase;
    private final GetAllPartiesUseCase getAllPartiesUseCase;
    private final SaveDealUseCase saveDealUseCase;
    private final GetAllDealsUseCase getAllDealsUseCase;

    private final PartyMapper partyMapper;

    @PostMapping("/party/save")
    @Operation
    public void saveParty(@RequestBody Party party) {
        log.info("saveParty(): {}", party);
        savePartyUseCase.execute(party);
    }

    @GetMapping("/party/list")
    public List<PartyDto> getAllParties() {
        return getAllPartiesUseCase.execute()
            .stream()
            .map(party -> switch (party.getClass().getSimpleName()) {//костыль
                case "Individual" -> partyMapper.indToDto((Individual) party);
                case "LegalEntity" -> partyMapper.leToDto((LegalEntity) party);
                default -> null;
            })
            .collect(Collectors.toList());
    }

    @PostMapping("/deal/save")
    @Operation
    public void saveDeal(@RequestBody Deal deal) {
        log.info("saveDeal(): {}", deal);
        saveDealUseCase.execute(deal);
    }

    @GetMapping("/deal/list")
    public List<Deal> getAllDeals() {
        return getAllDealsUseCase.execute();
    }
}
