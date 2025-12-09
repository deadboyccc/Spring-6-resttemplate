package dev.dead.spring6resttemplate.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    void listBeers() {
        var beers = beerClient.listBeers();
        log.debug("Beers: {}", beers.getContent());


    }
}