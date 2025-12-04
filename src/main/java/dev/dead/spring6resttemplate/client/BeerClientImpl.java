package dev.dead.spring6resttemplate.client;

import dev.dead.spring6resttemplate.models.BeerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class BeerClientImpl implements BeerClient {
    private final RestTemplateBuilder restTemplateBuilder;

    @Override
    public Page<BeerDTO> listBeers() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/v1/beer",
                String.class);

        log.info("Status Code: {}", responseEntity.getStatusCode());
        log.debug("Response Body: {}", responseEntity.getBody());

        // TODO: Parse the JSON response and convert to Page<BeerDTO>
        return null;
    }
}