package dev.dead.spring6resttemplate.client;

import dev.dead.spring6resttemplate.models.BeerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class BeerClientImpl implements BeerClient {
    private final RestTemplateBuilder restTemplateBuilder;
    private static final String BASE_URL = "http://localhost:8080";
    private final String GET_BEER_PATH = "/api/v1/beer";

    @Override
    public Page<BeerDTO> listBeers() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH,
                String.class);
        ResponseEntity<Map> responseEntityMap = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, Map.class);
        assert responseEntityMap.getBody() != null;
        log.debug("Response Entity Map: {}", responseEntityMap.getBody()
                .size());


        log.info("Status Code: {}", responseEntity.getStatusCode());
        log.debug("Response Body: {}", responseEntity.getBody());

        return Page.empty();
    }
}