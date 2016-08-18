package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Fonctionnalite;
import com.afrologix.skulman.repository.FonctionnaliteRepository;
import com.afrologix.skulman.repository.search.FonctionnaliteSearchRepository;
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
 * REST controller for managing Fonctionnalite.
 */
@RestController
@RequestMapping("/api")
public class FonctionnaliteResource {

    private final Logger log = LoggerFactory.getLogger(FonctionnaliteResource.class);
        
    @Inject
    private FonctionnaliteRepository fonctionnaliteRepository;
    
    @Inject
    private FonctionnaliteSearchRepository fonctionnaliteSearchRepository;
    
    /**
     * POST  /fonctionnalites : Create a new fonctionnalite.
     *
     * @param fonctionnalite the fonctionnalite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fonctionnalite, or with status 400 (Bad Request) if the fonctionnalite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fonctionnalites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fonctionnalite> createFonctionnalite(@Valid @RequestBody Fonctionnalite fonctionnalite) throws URISyntaxException {
        log.debug("REST request to save Fonctionnalite : {}", fonctionnalite);
        if (fonctionnalite.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fonctionnalite", "idexists", "A new fonctionnalite cannot already have an ID")).body(null);
        }
        Fonctionnalite result = fonctionnaliteRepository.save(fonctionnalite);
        fonctionnaliteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fonctionnalites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fonctionnalite", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fonctionnalites : Updates an existing fonctionnalite.
     *
     * @param fonctionnalite the fonctionnalite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fonctionnalite,
     * or with status 400 (Bad Request) if the fonctionnalite is not valid,
     * or with status 500 (Internal Server Error) if the fonctionnalite couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fonctionnalites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fonctionnalite> updateFonctionnalite(@Valid @RequestBody Fonctionnalite fonctionnalite) throws URISyntaxException {
        log.debug("REST request to update Fonctionnalite : {}", fonctionnalite);
        if (fonctionnalite.getId() == null) {
            return createFonctionnalite(fonctionnalite);
        }
        Fonctionnalite result = fonctionnaliteRepository.save(fonctionnalite);
        fonctionnaliteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fonctionnalite", fonctionnalite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fonctionnalites : get all the fonctionnalites.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fonctionnalites in body
     */
    @RequestMapping(value = "/fonctionnalites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Fonctionnalite> getAllFonctionnalites() {
        log.debug("REST request to get all Fonctionnalites");
        List<Fonctionnalite> fonctionnalites = fonctionnaliteRepository.findAll();
        return fonctionnalites;
    }

    /**
     * GET  /fonctionnalites/:id : get the "id" fonctionnalite.
     *
     * @param id the id of the fonctionnalite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fonctionnalite, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/fonctionnalites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fonctionnalite> getFonctionnalite(@PathVariable Long id) {
        log.debug("REST request to get Fonctionnalite : {}", id);
        Fonctionnalite fonctionnalite = fonctionnaliteRepository.findOne(id);
        return Optional.ofNullable(fonctionnalite)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fonctionnalites/:id : delete the "id" fonctionnalite.
     *
     * @param id the id of the fonctionnalite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/fonctionnalites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFonctionnalite(@PathVariable Long id) {
        log.debug("REST request to delete Fonctionnalite : {}", id);
        fonctionnaliteRepository.delete(id);
        fonctionnaliteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fonctionnalite", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fonctionnalites?query=:query : search for the fonctionnalite corresponding
     * to the query.
     *
     * @param query the query of the fonctionnalite search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/fonctionnalites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Fonctionnalite> searchFonctionnalites(@RequestParam String query) {
        log.debug("REST request to search Fonctionnalites for query {}", query);
        return StreamSupport
            .stream(fonctionnaliteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
