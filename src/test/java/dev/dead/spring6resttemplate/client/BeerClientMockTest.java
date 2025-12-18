package dev.dead.spring6resttemplate.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dead.spring6resttemplate.config.RestTemplateBuilderConfig;
import dev.dead.spring6resttemplate.models.BeerDTO;
import dev.dead.spring6resttemplate.models.BeerDTOPageImpl;
import dev.dead.spring6resttemplate.models.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@Slf4j
@RestClientTest
@Import(RestTemplateBuilderConfig.class)
public class BeerClientMockTest {

    static final String URL = "http://localhost:8080";

    BeerClient beerClient;

    MockRestServiceServer server;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RestTemplateBuilder mockRestTemplateBuilder;

    RestTemplate restTemplate;

    BeerDTO dto;
    String dtoJson;


    @BeforeEach
    void setUp() throws JsonProcessingException {
        MockServerRestTemplateCustomizer customizer = new MockServerRestTemplateCustomizer();
        RestTemplateBuilder builder = new RestTemplateBuilder(customizer).rootUri(URL);
        restTemplate = builder.build();
        server = customizer.getServer();

        when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);
        beerClient = new BeerClientImpl(mockRestTemplateBuilder);

        dto = getBeerDto();
        dtoJson = objectMapper.writeValueAsString(dto);
    }

    @Test
    void testDeleteBeerById() {
        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestToUriTemplate(BeerClientImpl.GET_BEER_BY_ID_PATH, dto.getId()))
                .andRespond(withNoContent());
        beerClient.deleteBeer(dto.getId());
        server.verify();
    }

    @Test
    void testUpdateBeerById() {

        server.expect(method(HttpMethod.PUT))
                .andExpect(requestToUriTemplate(BeerClientImpl.GET_BEER_BY_ID_PATH, dto.getId()))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));
        mockGetOperation();
        var updatedDto = beerClient.updateBeer(dto.getId(), dto);
        assertThat(updatedDto).isNotNull();
        assertThat(updatedDto.getId()).isEqualTo(dto.getId());


    }

    @Test
    void testCreateBeer() {
        URI uri =
                UriComponentsBuilder.fromPath(BeerClientImpl.GET_BEER_BY_ID_PATH)
                        .build(dto.getId());
        log.debug("URI: {}", uri);

        server.expect(method(HttpMethod.POST))
                .andExpect(requestTo(BeerClientImpl.GET_BEER_PATH))
                .andRespond(withAccepted().location(uri));

        mockGetOperation();

        var createdDto = beerClient.createBeer(dto);
        log.debug("Created Beer: {}", createdDto);
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isEqualTo(dto.getId());
        assertThat(createdDto.getPrice()).isEqualTo(dto.getPrice());
        assertThat(createdDto.getBeerName()).isEqualTo(dto.getBeerName());
        assertThat(createdDto.getBeerStyle()).isEqualTo(dto.getBeerStyle());
        assertThat(createdDto.getQuantityOnHand()).isEqualTo(dto.getQuantityOnHand());
        assertThat(createdDto.getUpc()).isEqualTo(dto.getUpc());


    }

    private void mockGetOperation() {
        server.expect(method(HttpMethod.GET))
                .andExpect(requestToUriTemplate(BeerClientImpl.GET_BEER_BY_ID_PATH, dto.getId()))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetBeerById() {

        mockGetOperation();

        var response = beerClient.getBeerById(dto.getId());
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(dto.getId());

    }

    @Test
    void testListBeers() throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(getPage());

        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(BeerClientImpl.GET_BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> dtos = beerClient.listBeers();
        assertThat(dtos.getContent()
                .size()).isGreaterThan(0);
    }

    BeerDTO getBeerDto() {
        return BeerDTO.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();
    }

    BeerDTOPageImpl getPage() {
        return new BeerDTOPageImpl(Collections.singletonList(getBeerDto()), 1, 25, 1);
    }
}