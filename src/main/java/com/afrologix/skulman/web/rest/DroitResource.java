package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Droit;
import com.afrologix.skulman.repository.DroitRepository;
import com.afrologix.skulman.repository.search.DroitSearchRepository;
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
 * REST controller for managing Droit.
 */
@RestController
@RequestMapping("/api")
public class DroitResource {

    private final Logger log = LoggerFactory.getLogger(DroitResource.class);
        
    @Inject
    private DroitRepository droitRepository;
    
    @Inject
    private DroitSearchRepository droitSearchRepository;
    
    /**
     * POST  /droits : Create a new droit.
     *
     * @param droit the droit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new droit, or with status 400 (Bad Request) if the droit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/droits",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Droit> createDroit(@Valid @RequestBody Droit droit) throws URISyntaxException {
        log.debug("REST request to save Droit : {}", droit);
        if (droit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("droit", "idexists", "A new droit cannot already have an ID")).body(null);
        }
        Droit result = droitRepository.save(droit);
        droitSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/droits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("droit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /droits : Updates an existing droit.
     *
     * @param droit the droit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated droit,
     * or with status 400 (Bad Request) if the droit is not valid,
     * or with status 500 (Internal Server Error) if the droit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/droits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Droit> updateDroit(@Valid @RequestBody Droit droit) throws URISyntaxException {
        log.debug("REST request to update Droit : {}", droit);
        if (droit.getId() == null) {
            return createDroit(droit);
        }
        Droit result = droitRepository.save(droit);
        droitSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("droit", droit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /droits : get all the droits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of droits in body
     */
    @RequestMapping(value = "/droits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Droit> getAllDroits() {
        log.debug("REST request to get all Droits");
        List<Droit> droits = droitRepository.findAll();
        return droits;
    }

    /**
     * GET  /droits/:id : get the "id" droit.
     *
     * @param id the id of the droit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the droit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/droits/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Droit> getDroit(@PathVariable Long id) {
        log.debug("REST request to get Droit : {}", id);
        Droit droit = droitRepository.findOne(id);
        return Optional.ofNullable(droit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /droits/:id : delete the "id" droit.
     *
     * @param id the id of the droit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/droits/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDroit(@PathVariable Long id) {
        log.debug("REST request to delete Droit : {}", id);
        droitRepository.delete(id);
        droitSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("droit", id.toString())).build();
    }

    /**
     * SEARCH  /_search/droits?query=:query : search for the droit corresponding
     * to the query.
     *
     * @param query the query of the droit search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/droits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Droit> searchDroits(@RequestParam String query) {
        log.debug("REST request to search Droits for query {}", query);
        return StreamSupport
            .stream(droitSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
