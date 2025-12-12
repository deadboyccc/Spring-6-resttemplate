package dev.dead.spring6resttemplate.client;

import com.fasterxml.jackson.databind.JsonNode;
import dev.dead.spring6resttemplate.models.BeerDTO;
import dev.dead.spring6resttemplate.models.BeerDTOPageImpl;
import dev.dead.spring6resttemplate.models.BeerStyle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public abstract class BeerClientImplOldDemo implements BeerClient {
    private final RestTemplateBuilder restTemplateBuilder;
    private final String GET_BEER_PATH = "/api/v1/beer";

    @Override
    public Page<BeerDTO> listBeers() {
        return this.listBeers(null, null, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
                                   Integer pageSize) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(GET_BEER_PATH,
                String.class);
        ResponseEntity<Map> responseEntityMap = restTemplate.getForEntity(GET_BEER_PATH, Map.class);
        assert responseEntityMap.getBody() != null;
        ResponseEntity<JsonNode> responseEntityJsonNode = restTemplate.getForEntity(GET_BEER_PATH, JsonNode.class);
        assert responseEntityJsonNode.getBody() != null;
        responseEntityJsonNode.getBody()
                .findPath("content")
                .forEach(node -> log.debug("Node Beer Name: {}", node.get("beerName")
                        .asText()));
        log.debug("Response Entity Map: {}", responseEntityMap.getBody()
                .size());


        log.info("Status Code: {}", responseEntity.getStatusCode());
        log.debug("Response Body: {}", responseEntity.getBody());
        ResponseEntity<BeerDTOPageImpl> pageResponseEntity =
                restTemplate.getForEntity(GET_BEER_PATH, BeerDTOPageImpl.class);

        return pageResponseEntity.getBody();
    }
}