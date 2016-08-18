package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Evenement;
import com.afrologix.skulman.repository.EvenementRepository;
import com.afrologix.skulman.repository.search.EvenementSearchRepository;
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
 * REST controller for managing Evenement.
 */
@RestController
@RequestMapping("/api")
public class EvenementResource {

    private final Logger log = LoggerFactory.getLogger(EvenementResource.class);
        
    @Inject
    private EvenementRepository evenementRepository;
    
    @Inject
    private EvenementSearchRepository evenementSearchRepository;
    
    /**
     * POST  /evenements : Create a new evenement.
     *
     * @param evenement the evenement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new evenement, or with status 400 (Bad Request) if the evenement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/evenements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Evenement> createEvenement(@Valid @RequestBody Evenement evenement) throws URISyntaxException {
        log.debug("REST request to save Evenement : {}", evenement);
        if (evenement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("evenement", "idexists", "A new evenement cannot already have an ID")).body(null);
        }
        Evenement result = evenementRepository.save(evenement);
        evenementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/evenements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("evenement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /evenements : Updates an existing evenement.
     *
     * @param evenement the evenement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated evenement,
     * or with status 400 (Bad Request) if the evenement is not valid,
     * or with status 500 (Internal Server Error) if the evenement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/evenements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Evenement> updateEvenement(@Valid @RequestBody Evenement evenement) throws URISyntaxException {
        log.debug("REST request to update Evenement : {}", evenement);
        if (evenement.getId() == null) {
            return createEvenement(evenement);
        }
        Evenement result = evenementRepository.save(evenement);
        evenementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("evenement", evenement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /evenements : get all the evenements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of evenements in body
     */
    @RequestMapping(value = "/evenements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Evenement> getAllEvenements() {
        log.debug("REST request to get all Evenements");
        List<Evenement> evenements = evenementRepository.findAll();
        return evenements;
    }

    /**
     * GET  /evenements/:id : get the "id" evenement.
     *
     * @param id the id of the evenement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the evenement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/evenements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Evenement> getEvenement(@PathVariable Long id) {
        log.debug("REST request to get Evenement : {}", id);
        Evenement evenement = evenementRepository.findOne(id);
        return Optional.ofNullable(evenement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /evenements/:id : delete the "id" evenement.
     *
     * @param id the id of the evenement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/evenements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        log.debug("REST request to delete Evenement : {}", id);
        evenementRepository.delete(id);
        evenementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("evenement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/evenements?query=:query : search for the evenement corresponding
     * to the query.
     *
     * @param query the query of the evenement search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/evenements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Evenement> searchEvenements(@RequestParam String query) {
        log.debug("REST request to search Evenements for query {}", query);
        return StreamSupport
            .stream(evenementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
