package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.TarifBus;
import com.afrologix.skulman.repository.TarifBusRepository;
import com.afrologix.skulman.repository.search.TarifBusSearchRepository;
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
 * REST controller for managing TarifBus.
 */
@RestController
@RequestMapping("/api")
public class TarifBusResource {

    private final Logger log = LoggerFactory.getLogger(TarifBusResource.class);
        
    @Inject
    private TarifBusRepository tarifBusRepository;
    
    @Inject
    private TarifBusSearchRepository tarifBusSearchRepository;
    
    /**
     * POST  /tarif-buses : Create a new tarifBus.
     *
     * @param tarifBus the tarifBus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tarifBus, or with status 400 (Bad Request) if the tarifBus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tarif-buses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TarifBus> createTarifBus(@Valid @RequestBody TarifBus tarifBus) throws URISyntaxException {
        log.debug("REST request to save TarifBus : {}", tarifBus);
        if (tarifBus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tarifBus", "idexists", "A new tarifBus cannot already have an ID")).body(null);
        }
        TarifBus result = tarifBusRepository.save(tarifBus);
        tarifBusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tarif-buses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tarifBus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tarif-buses : Updates an existing tarifBus.
     *
     * @param tarifBus the tarifBus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tarifBus,
     * or with status 400 (Bad Request) if the tarifBus is not valid,
     * or with status 500 (Internal Server Error) if the tarifBus couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tarif-buses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TarifBus> updateTarifBus(@Valid @RequestBody TarifBus tarifBus) throws URISyntaxException {
        log.debug("REST request to update TarifBus : {}", tarifBus);
        if (tarifBus.getId() == null) {
            return createTarifBus(tarifBus);
        }
        TarifBus result = tarifBusRepository.save(tarifBus);
        tarifBusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tarifBus", tarifBus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tarif-buses : get all the tarifBuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tarifBuses in body
     */
    @RequestMapping(value = "/tarif-buses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TarifBus> getAllTarifBuses() {
        log.debug("REST request to get all TarifBuses");
        List<TarifBus> tarifBuses = tarifBusRepository.findAll();
        return tarifBuses;
    }

    /**
     * GET  /tarif-buses/:id : get the "id" tarifBus.
     *
     * @param id the id of the tarifBus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tarifBus, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tarif-buses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TarifBus> getTarifBus(@PathVariable Long id) {
        log.debug("REST request to get TarifBus : {}", id);
        TarifBus tarifBus = tarifBusRepository.findOne(id);
        return Optional.ofNullable(tarifBus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tarif-buses/:id : delete the "id" tarifBus.
     *
     * @param id the id of the tarifBus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tarif-buses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTarifBus(@PathVariable Long id) {
        log.debug("REST request to delete TarifBus : {}", id);
        tarifBusRepository.delete(id);
        tarifBusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tarifBus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/tarif-buses?query=:query : search for the tarifBus corresponding
     * to the query.
     *
     * @param query the query of the tarifBus search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/tarif-buses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TarifBus> searchTarifBuses(@RequestParam String query) {
        log.debug("REST request to search TarifBuses for query {}", query);
        return StreamSupport
            .stream(tarifBusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
