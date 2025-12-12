package dev.dead.spring6resttemplate.client;

import dev.dead.spring6resttemplate.models.BeerDTO;
import dev.dead.spring6resttemplate.models.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {
    BeerDTO getBeerById(UUID beerId);
    Page<BeerDTO> listBeers();

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
                            Integer pageSize);
}
