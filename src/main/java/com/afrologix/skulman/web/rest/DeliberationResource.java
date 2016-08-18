package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Deliberation;
import com.afrologix.skulman.repository.DeliberationRepository;
import com.afrologix.skulman.repository.search.DeliberationSearchRepository;
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
 * REST controller for managing Deliberation.
 */
@RestController
@RequestMapping("/api")
public class DeliberationResource {

    private final Logger log = LoggerFactory.getLogger(DeliberationResource.class);
        
    @Inject
    private DeliberationRepository deliberationRepository;
    
    @Inject
    private DeliberationSearchRepository deliberationSearchRepository;
    
    /**
     * POST  /deliberations : Create a new deliberation.
     *
     * @param deliberation the deliberation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deliberation, or with status 400 (Bad Request) if the deliberation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/deliberations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deliberation> createDeliberation(@Valid @RequestBody Deliberation deliberation) throws URISyntaxException {
        log.debug("REST request to save Deliberation : {}", deliberation);
        if (deliberation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("deliberation", "idexists", "A new deliberation cannot already have an ID")).body(null);
        }
        Deliberation result = deliberationRepository.save(deliberation);
        deliberationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/deliberations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("deliberation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /deliberations : Updates an existing deliberation.
     *
     * @param deliberation the deliberation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deliberation,
     * or with status 400 (Bad Request) if the deliberation is not valid,
     * or with status 500 (Internal Server Error) if the deliberation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/deliberations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deliberation> updateDeliberation(@Valid @RequestBody Deliberation deliberation) throws URISyntaxException {
        log.debug("REST request to update Deliberation : {}", deliberation);
        if (deliberation.getId() == null) {
            return createDeliberation(deliberation);
        }
        Deliberation result = deliberationRepository.save(deliberation);
        deliberationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("deliberation", deliberation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /deliberations : get all the deliberations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of deliberations in body
     */
    @RequestMapping(value = "/deliberations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Deliberation> getAllDeliberations() {
        log.debug("REST request to get all Deliberations");
        List<Deliberation> deliberations = deliberationRepository.findAll();
        return deliberations;
    }

    /**
     * GET  /deliberations/:id : get the "id" deliberation.
     *
     * @param id the id of the deliberation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deliberation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/deliberations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deliberation> getDeliberation(@PathVariable Long id) {
        log.debug("REST request to get Deliberation : {}", id);
        Deliberation deliberation = deliberationRepository.findOne(id);
        return Optional.ofNullable(deliberation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /deliberations/:id : delete the "id" deliberation.
     *
     * @param id the id of the deliberation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/deliberations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDeliberation(@PathVariable Long id) {
        log.debug("REST request to delete Deliberation : {}", id);
        deliberationRepository.delete(id);
        deliberationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("deliberation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/deliberations?query=:query : search for the deliberation corresponding
     * to the query.
     *
     * @param query the query of the deliberation search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/deliberations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Deliberation> searchDeliberations(@RequestParam String query) {
        log.debug("REST request to search Deliberations for query {}", query);
        return StreamSupport
            .stream(deliberationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
