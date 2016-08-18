package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Bus;
import com.afrologix.skulman.repository.BusRepository;
import com.afrologix.skulman.repository.search.BusSearchRepository;
import com.afrologix.skulman.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Bus.
 */
@RestController
@RequestMapping("/api")
public class BusResource {

    private final Logger log = LoggerFactory.getLogger(BusResource.class);
        
    @Inject
    private BusRepository busRepository;
    
    @Inject
    private BusSearchRepository busSearchRepository;
    
    /**
     * POST  /buses : Create a new bus.
     *
     * @param bus the bus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bus, or with status 400 (Bad Request) if the bus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/buses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bus> createBus(@Valid @RequestBody Bus bus) throws URISyntaxException {
        log.debug("REST request to save Bus : {}", bus);
        if (bus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bus", "idexists", "A new bus cannot already have an ID")).body(null);
        }
        Bus result = busRepository.save(bus);
        busSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/buses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /buses : Updates an existing bus.
     *
     * @param bus the bus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bus,
     * or with status 400 (Bad Request) if the bus is not valid,
     * or with status 500 (Internal Server Error) if the bus couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/buses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bus> updateBus(@Valid @RequestBody Bus bus) throws URISyntaxException {
        log.debug("REST request to update Bus : {}", bus);
        if (bus.getId() == null) {
            return createBus(bus);
        }
        Bus result = busRepository.save(bus);
        busSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bus", bus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /buses : get all the buses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of buses in body
     */
    @RequestMapping(value = "/buses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Bus> getAllBuses() {
        log.debug("REST request to get all Buses");
        List<Bus> buses = busRepository.findAll();
        return buses;
    }

    /**
     * GET  /buses/:id : get the "id" bus.
     *
     * @param id the id of the bus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bus, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/buses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bus> getBus(@PathVariable Long id) {
        log.debug("REST request to get Bus : {}", id);
        Bus bus = busRepository.findOne(id);
        return Optional.ofNullable(bus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /buses/:id : delete the "id" bus.
     *
     * @param id the id of the bus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/buses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        log.debug("REST request to delete Bus : {}", id);
        busRepository.delete(id);
        busSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/buses?query=:query : search for the bus corresponding
     * to the query.
     *
     * @param query the query of the bus search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/buses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Bus> searchBuses(@RequestParam String query) {
        log.debug("REST request to search Buses for query {}", query);
        return StreamSupport
            .stream(busSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
