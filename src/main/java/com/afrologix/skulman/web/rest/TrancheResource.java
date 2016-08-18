package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Tranche;
import com.afrologix.skulman.repository.TrancheRepository;
import com.afrologix.skulman.repository.search.TrancheSearchRepository;
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
 * REST controller for managing Tranche.
 */
@RestController
@RequestMapping("/api")
public class TrancheResource {

    private final Logger log = LoggerFactory.getLogger(TrancheResource.class);
        
    @Inject
    private TrancheRepository trancheRepository;
    
    @Inject
    private TrancheSearchRepository trancheSearchRepository;
    
    /**
     * POST  /tranches : Create a new tranche.
     *
     * @param tranche the tranche to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tranche, or with status 400 (Bad Request) if the tranche has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tranches",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tranche> createTranche(@Valid @RequestBody Tranche tranche) throws URISyntaxException {
        log.debug("REST request to save Tranche : {}", tranche);
        if (tranche.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tranche", "idexists", "A new tranche cannot already have an ID")).body(null);
        }
        Tranche result = trancheRepository.save(tranche);
        trancheSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tranches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tranche", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tranches : Updates an existing tranche.
     *
     * @param tranche the tranche to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tranche,
     * or with status 400 (Bad Request) if the tranche is not valid,
     * or with status 500 (Internal Server Error) if the tranche couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tranches",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tranche> updateTranche(@Valid @RequestBody Tranche tranche) throws URISyntaxException {
        log.debug("REST request to update Tranche : {}", tranche);
        if (tranche.getId() == null) {
            return createTranche(tranche);
        }
        Tranche result = trancheRepository.save(tranche);
        trancheSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tranche", tranche.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tranches : get all the tranches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tranches in body
     */
    @RequestMapping(value = "/tranches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tranche> getAllTranches() {
        log.debug("REST request to get all Tranches");
        List<Tranche> tranches = trancheRepository.findAll();
        return tranches;
    }

    /**
     * GET  /tranches/:id : get the "id" tranche.
     *
     * @param id the id of the tranche to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tranche, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tranches/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tranche> getTranche(@PathVariable Long id) {
        log.debug("REST request to get Tranche : {}", id);
        Tranche tranche = trancheRepository.findOne(id);
        return Optional.ofNullable(tranche)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tranches/:id : delete the "id" tranche.
     *
     * @param id the id of the tranche to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tranches/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTranche(@PathVariable Long id) {
        log.debug("REST request to delete Tranche : {}", id);
        trancheRepository.delete(id);
        trancheSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tranche", id.toString())).build();
    }

    /**
     * SEARCH  /_search/tranches?query=:query : search for the tranche corresponding
     * to the query.
     *
     * @param query the query of the tranche search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/tranches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tranche> searchTranches(@RequestParam String query) {
        log.debug("REST request to search Tranches for query {}", query);
        return StreamSupport
            .stream(trancheSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
