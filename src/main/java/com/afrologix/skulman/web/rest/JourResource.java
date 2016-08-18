package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Jour;
import com.afrologix.skulman.repository.JourRepository;
import com.afrologix.skulman.repository.search.JourSearchRepository;
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
 * REST controller for managing Jour.
 */
@RestController
@RequestMapping("/api")
public class JourResource {

    private final Logger log = LoggerFactory.getLogger(JourResource.class);
        
    @Inject
    private JourRepository jourRepository;
    
    @Inject
    private JourSearchRepository jourSearchRepository;
    
    /**
     * POST  /jours : Create a new jour.
     *
     * @param jour the jour to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jour, or with status 400 (Bad Request) if the jour has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/jours",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Jour> createJour(@Valid @RequestBody Jour jour) throws URISyntaxException {
        log.debug("REST request to save Jour : {}", jour);
        if (jour.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("jour", "idexists", "A new jour cannot already have an ID")).body(null);
        }
        Jour result = jourRepository.save(jour);
        jourSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/jours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("jour", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jours : Updates an existing jour.
     *
     * @param jour the jour to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jour,
     * or with status 400 (Bad Request) if the jour is not valid,
     * or with status 500 (Internal Server Error) if the jour couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/jours",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Jour> updateJour(@Valid @RequestBody Jour jour) throws URISyntaxException {
        log.debug("REST request to update Jour : {}", jour);
        if (jour.getId() == null) {
            return createJour(jour);
        }
        Jour result = jourRepository.save(jour);
        jourSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("jour", jour.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jours : get all the jours.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jours in body
     */
    @RequestMapping(value = "/jours",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Jour> getAllJours() {
        log.debug("REST request to get all Jours");
        List<Jour> jours = jourRepository.findAll();
        return jours;
    }

    /**
     * GET  /jours/:id : get the "id" jour.
     *
     * @param id the id of the jour to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jour, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/jours/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Jour> getJour(@PathVariable Long id) {
        log.debug("REST request to get Jour : {}", id);
        Jour jour = jourRepository.findOne(id);
        return Optional.ofNullable(jour)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jours/:id : delete the "id" jour.
     *
     * @param id the id of the jour to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/jours/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteJour(@PathVariable Long id) {
        log.debug("REST request to delete Jour : {}", id);
        jourRepository.delete(id);
        jourSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("jour", id.toString())).build();
    }

    /**
     * SEARCH  /_search/jours?query=:query : search for the jour corresponding
     * to the query.
     *
     * @param query the query of the jour search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/jours",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Jour> searchJours(@RequestParam String query) {
        log.debug("REST request to search Jours for query {}", query);
        return StreamSupport
            .stream(jourSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
