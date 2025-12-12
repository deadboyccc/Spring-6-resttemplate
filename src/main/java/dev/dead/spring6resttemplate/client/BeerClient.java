package dev.dead.spring6resttemplate.client;

import dev.dead.spring6resttemplate.models.BeerDTO;
import dev.dead.spring6resttemplate.models.BeerStyle;
import org.springframework.data.domain.Page;

public interface BeerClient {
    Page<BeerDTO> listBeers();

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
                            Integer pageSize);
}
