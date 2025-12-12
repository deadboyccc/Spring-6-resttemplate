package dev.dead.spring6resttemplate.client;

import dev.dead.spring6resttemplate.models.BeerDTO;
import dev.dead.spring6resttemplate.models.BeerDTOPageImpl;
import dev.dead.spring6resttemplate.models.BeerStyle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BeerClientImpl implements BeerClient {
    private final RestTemplateBuilder restTemplateBuilder;
    private final String GET_BEER_PATH = "/api/v1/beer";
    private final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";

    @Override
    public BeerDTO getBeerById(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<BeerDTO> responseEntity = restTemplate.getForEntity(GET_BEER_BY_ID_PATH,
                BeerDTO.class, beerId);
        return responseEntity.getBody();
    }

    @Override
    public Page<BeerDTO> listBeers() {

        return this.listBeers(null, null, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
                                   Integer pageSize) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);
        if (beerName != null && !beerName.isBlank()) {
            uriComponentsBuilder.queryParam("beerName", beerName);
        }
        if (beerStyle != null) {
            uriComponentsBuilder.queryParam("beerStyle", beerStyle);
        }
        if (showInventory != null) {
            uriComponentsBuilder.queryParam("showInventory", showInventory);
        }
        if (pageNumber != null) {
            uriComponentsBuilder.queryParam("pageNumber", pageNumber);
        }
        if (pageSize != null) {
            uriComponentsBuilder.queryParam("pageSize", pageSize);
        }
        ResponseEntity<BeerDTOPageImpl> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.toUriString()
                , BeerDTOPageImpl.class);
        return responseEntity.getBody();
    }
}