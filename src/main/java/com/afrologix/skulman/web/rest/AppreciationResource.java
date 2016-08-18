package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Appreciation;
import com.afrologix.skulman.repository.AppreciationRepository;
import com.afrologix.skulman.repository.search.AppreciationSearchRepository;
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
 * REST controller for managing Appreciation.
 */
@RestController
@RequestMapping("/api")
public class AppreciationResource {

    private final Logger log = LoggerFactory.getLogger(AppreciationResource.class);
        
    @Inject
    private AppreciationRepository appreciationRepository;
    
    @Inject
    private AppreciationSearchRepository appreciationSearchRepository;
    
    /**
     * POST  /appreciations : Create a new appreciation.
     *
     * @param appreciation the appreciation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appreciation, or with status 400 (Bad Request) if the appreciation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/appreciations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Appreciation> createAppreciation(@Valid @RequestBody Appreciation appreciation) throws URISyntaxException {
        log.debug("REST request to save Appreciation : {}", appreciation);
        if (appreciation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("appreciation", "idexists", "A new appreciation cannot already have an ID")).body(null);
        }
        Appreciation result = appreciationRepository.save(appreciation);
        appreciationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/appreciations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("appreciation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /appreciations : Updates an existing appreciation.
     *
     * @param appreciation the appreciation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appreciation,
     * or with status 400 (Bad Request) if the appreciation is not valid,
     * or with status 500 (Internal Server Error) if the appreciation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/appreciations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Appreciation> updateAppreciation(@Valid @RequestBody Appreciation appreciation) throws URISyntaxException {
        log.debug("REST request to update Appreciation : {}", appreciation);
        if (appreciation.getId() == null) {
            return createAppreciation(appreciation);
        }
        Appreciation result = appreciationRepository.save(appreciation);
        appreciationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("appreciation", appreciation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /appreciations : get all the appreciations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of appreciations in body
     */
    @RequestMapping(value = "/appreciations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Appreciation> getAllAppreciations() {
        log.debug("REST request to get all Appreciations");
        List<Appreciation> appreciations = appreciationRepository.findAll();
        return appreciations;
    }

    /**
     * GET  /appreciations/:id : get the "id" appreciation.
     *
     * @param id the id of the appreciation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appreciation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/appreciations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Appreciation> getAppreciation(@PathVariable Long id) {
        log.debug("REST request to get Appreciation : {}", id);
        Appreciation appreciation = appreciationRepository.findOne(id);
        return Optional.ofNullable(appreciation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /appreciations/:id : delete the "id" appreciation.
     *
     * @param id the id of the appreciation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/appreciations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAppreciation(@PathVariable Long id) {
        log.debug("REST request to delete Appreciation : {}", id);
        appreciationRepository.delete(id);
        appreciationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("appreciation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/appreciations?query=:query : search for the appreciation corresponding
     * to the query.
     *
     * @param query the query of the appreciation search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/appreciations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Appreciation> searchAppreciations(@RequestParam String query) {
        log.debug("REST request to search Appreciations for query {}", query);
        return StreamSupport
            .stream(appreciationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
