package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Cycle;
import com.afrologix.skulman.repository.CycleRepository;
import com.afrologix.skulman.repository.search.CycleSearchRepository;
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
 * REST controller for managing Cycle.
 */
@RestController
@RequestMapping("/api")
public class CycleResource {

    private final Logger log = LoggerFactory.getLogger(CycleResource.class);
        
    @Inject
    private CycleRepository cycleRepository;
    
    @Inject
    private CycleSearchRepository cycleSearchRepository;
    
    /**
     * POST  /cycles : Create a new cycle.
     *
     * @param cycle the cycle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cycle, or with status 400 (Bad Request) if the cycle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cycles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cycle> createCycle(@Valid @RequestBody Cycle cycle) throws URISyntaxException {
        log.debug("REST request to save Cycle : {}", cycle);
        if (cycle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cycle", "idexists", "A new cycle cannot already have an ID")).body(null);
        }
        Cycle result = cycleRepository.save(cycle);
        cycleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cycles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cycle", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cycles : Updates an existing cycle.
     *
     * @param cycle the cycle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cycle,
     * or with status 400 (Bad Request) if the cycle is not valid,
     * or with status 500 (Internal Server Error) if the cycle couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cycles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cycle> updateCycle(@Valid @RequestBody Cycle cycle) throws URISyntaxException {
        log.debug("REST request to update Cycle : {}", cycle);
        if (cycle.getId() == null) {
            return createCycle(cycle);
        }
        Cycle result = cycleRepository.save(cycle);
        cycleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cycle", cycle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cycles : get all the cycles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cycles in body
     */
    @RequestMapping(value = "/cycles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cycle> getAllCycles() {
        log.debug("REST request to get all Cycles");
        List<Cycle> cycles = cycleRepository.findAll();
        return cycles;
    }

    /**
     * GET  /cycles/:id : get the "id" cycle.
     *
     * @param id the id of the cycle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cycle, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cycles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cycle> getCycle(@PathVariable Long id) {
        log.debug("REST request to get Cycle : {}", id);
        Cycle cycle = cycleRepository.findOne(id);
        return Optional.ofNullable(cycle)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cycles/:id : delete the "id" cycle.
     *
     * @param id the id of the cycle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cycles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCycle(@PathVariable Long id) {
        log.debug("REST request to delete Cycle : {}", id);
        cycleRepository.delete(id);
        cycleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cycle", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cycles?query=:query : search for the cycle corresponding
     * to the query.
     *
     * @param query the query of the cycle search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/cycles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cycle> searchCycles(@RequestParam String query) {
        log.debug("REST request to search Cycles for query {}", query);
        return StreamSupport
            .stream(cycleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
