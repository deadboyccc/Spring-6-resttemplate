package dev.dead.spring6resttemplate.client;

import dev.dead.spring6resttemplate.models.BeerDTO;
import dev.dead.spring6resttemplate.models.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;

@Slf4j
@SpringBootTest
class BeerClientImplTest {
    @Autowired
    BeerClient beerClient;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void deleteBeer() {
        var beers = beerClient.listBeers();
        beerClient.deleteBeer(beers.getContent()
                .getFirst()
                .getId());
        assertThrows(HttpClientErrorException.class, () -> beerClient.getBeerById(beers.getContent()
                .getFirst()
                .getId()));

        log.debug("Beer Deleted");

    }

    @Test
    void testUpdateBeer() {
        var beers = beerClient.listBeers();
        var beer = beerClient.getBeerById(beers.getContent()
                .getFirst()
                .getId());
        assert beer != null;
        log.debug("Original Beer: {}", beer);
        beer.setBeerName("Updated Beer Name");
        var updatedBeer = beerClient.updateBeer(beer.getId(), beer);
        assert updatedBeer.getBeerName()
                .equals("Updated Beer Name");
        log.debug("Updated Beer: {}", updatedBeer);
    }

    @Test
    void testCreateBeer() {
        BeerDTO beerDTO =
                BeerDTO.builder()
                        .beerName("New Beer")
                        .beerStyle(BeerStyle.ALE)
                        .upc("123456789a012")
                        .price(BigDecimal.valueOf(9.99))
                        .quantityOnHand(30)
                        .build();
        var createdBeer = beerClient.createBeer(beerDTO);
        var beer = beerClient.getBeerById(createdBeer.getId());
        assert beer != null;
        assert beer.getBeerName()
                .equals("New Beer");
        assert beer.getBeerStyle()
                .equals(BeerStyle.ALE);
        assert beer.getPrice()
                .equals(BigDecimal.valueOf(9.99));
        assert beer.getQuantityOnHand() == 30;
        log.debug("Created Beer: {}", beerDTO);

    }

    @Test
    void getBeerById() {
        var beers = beerClient.listBeers();
        var beer = beerClient.getBeerById(beers.getContent()
                .getFirst()
                .getId());
        assert beer != null;
        log.debug("Beer: {}", beer);

    }

    @Test
    void listBeers() {
        var beers = beerClient.listBeers();
        log.debug("Beers: {}", beers.getSize());
    }

    @Test
    void listBeersWithBeerNameQueryParam() {
        var beers = beerClient.listBeers("ALE", null, null, null, null);
        log.debug("Beers: {}", beers.getSize());

    }
}