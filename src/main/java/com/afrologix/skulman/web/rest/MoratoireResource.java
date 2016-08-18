package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Moratoire;
import com.afrologix.skulman.repository.MoratoireRepository;
import com.afrologix.skulman.repository.search.MoratoireSearchRepository;
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
 * REST controller for managing Moratoire.
 */
@RestController
@RequestMapping("/api")
public class MoratoireResource {

    private final Logger log = LoggerFactory.getLogger(MoratoireResource.class);
        
    @Inject
    private MoratoireRepository moratoireRepository;
    
    @Inject
    private MoratoireSearchRepository moratoireSearchRepository;
    
    /**
     * POST  /moratoires : Create a new moratoire.
     *
     * @param moratoire the moratoire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new moratoire, or with status 400 (Bad Request) if the moratoire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/moratoires",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Moratoire> createMoratoire(@Valid @RequestBody Moratoire moratoire) throws URISyntaxException {
        log.debug("REST request to save Moratoire : {}", moratoire);
        if (moratoire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("moratoire", "idexists", "A new moratoire cannot already have an ID")).body(null);
        }
        Moratoire result = moratoireRepository.save(moratoire);
        moratoireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/moratoires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("moratoire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /moratoires : Updates an existing moratoire.
     *
     * @param moratoire the moratoire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated moratoire,
     * or with status 400 (Bad Request) if the moratoire is not valid,
     * or with status 500 (Internal Server Error) if the moratoire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/moratoires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Moratoire> updateMoratoire(@Valid @RequestBody Moratoire moratoire) throws URISyntaxException {
        log.debug("REST request to update Moratoire : {}", moratoire);
        if (moratoire.getId() == null) {
            return createMoratoire(moratoire);
        }
        Moratoire result = moratoireRepository.save(moratoire);
        moratoireSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("moratoire", moratoire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /moratoires : get all the moratoires.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of moratoires in body
     */
    @RequestMapping(value = "/moratoires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Moratoire> getAllMoratoires() {
        log.debug("REST request to get all Moratoires");
        List<Moratoire> moratoires = moratoireRepository.findAll();
        return moratoires;
    }

    /**
     * GET  /moratoires/:id : get the "id" moratoire.
     *
     * @param id the id of the moratoire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the moratoire, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/moratoires/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Moratoire> getMoratoire(@PathVariable Long id) {
        log.debug("REST request to get Moratoire : {}", id);
        Moratoire moratoire = moratoireRepository.findOne(id);
        return Optional.ofNullable(moratoire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /moratoires/:id : delete the "id" moratoire.
     *
     * @param id the id of the moratoire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/moratoires/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMoratoire(@PathVariable Long id) {
        log.debug("REST request to delete Moratoire : {}", id);
        moratoireRepository.delete(id);
        moratoireSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("moratoire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/moratoires?query=:query : search for the moratoire corresponding
     * to the query.
     *
     * @param query the query of the moratoire search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/moratoires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Moratoire> searchMoratoires(@RequestParam String query) {
        log.debug("REST request to search Moratoires for query {}", query);
        return StreamSupport
            .stream(moratoireSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
