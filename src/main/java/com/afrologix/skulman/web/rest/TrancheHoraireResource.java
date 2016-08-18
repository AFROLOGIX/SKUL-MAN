package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.TrancheHoraire;
import com.afrologix.skulman.repository.TrancheHoraireRepository;
import com.afrologix.skulman.repository.search.TrancheHoraireSearchRepository;
import com.afrologix.skulman.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TrancheHoraire.
 */
@RestController
@RequestMapping("/api")
public class TrancheHoraireResource {

    private final Logger log = LoggerFactory.getLogger(TrancheHoraireResource.class);
        
    @Inject
    private TrancheHoraireRepository trancheHoraireRepository;
    
    @Inject
    private TrancheHoraireSearchRepository trancheHoraireSearchRepository;
    
    /**
     * POST  /tranche-horaires : Create a new trancheHoraire.
     *
     * @param trancheHoraire the trancheHoraire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trancheHoraire, or with status 400 (Bad Request) if the trancheHoraire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tranche-horaires",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TrancheHoraire> createTrancheHoraire(@RequestBody TrancheHoraire trancheHoraire) throws URISyntaxException {
        log.debug("REST request to save TrancheHoraire : {}", trancheHoraire);
        if (trancheHoraire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("trancheHoraire", "idexists", "A new trancheHoraire cannot already have an ID")).body(null);
        }
        TrancheHoraire result = trancheHoraireRepository.save(trancheHoraire);
        trancheHoraireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tranche-horaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("trancheHoraire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tranche-horaires : Updates an existing trancheHoraire.
     *
     * @param trancheHoraire the trancheHoraire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trancheHoraire,
     * or with status 400 (Bad Request) if the trancheHoraire is not valid,
     * or with status 500 (Internal Server Error) if the trancheHoraire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tranche-horaires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TrancheHoraire> updateTrancheHoraire(@RequestBody TrancheHoraire trancheHoraire) throws URISyntaxException {
        log.debug("REST request to update TrancheHoraire : {}", trancheHoraire);
        if (trancheHoraire.getId() == null) {
            return createTrancheHoraire(trancheHoraire);
        }
        TrancheHoraire result = trancheHoraireRepository.save(trancheHoraire);
        trancheHoraireSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("trancheHoraire", trancheHoraire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tranche-horaires : get all the trancheHoraires.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of trancheHoraires in body
     */
    @RequestMapping(value = "/tranche-horaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TrancheHoraire> getAllTrancheHoraires() {
        log.debug("REST request to get all TrancheHoraires");
        List<TrancheHoraire> trancheHoraires = trancheHoraireRepository.findAll();
        return trancheHoraires;
    }

    /**
     * GET  /tranche-horaires/:id : get the "id" trancheHoraire.
     *
     * @param id the id of the trancheHoraire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trancheHoraire, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tranche-horaires/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TrancheHoraire> getTrancheHoraire(@PathVariable Long id) {
        log.debug("REST request to get TrancheHoraire : {}", id);
        TrancheHoraire trancheHoraire = trancheHoraireRepository.findOne(id);
        return Optional.ofNullable(trancheHoraire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tranche-horaires/:id : delete the "id" trancheHoraire.
     *
     * @param id the id of the trancheHoraire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tranche-horaires/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTrancheHoraire(@PathVariable Long id) {
        log.debug("REST request to delete TrancheHoraire : {}", id);
        trancheHoraireRepository.delete(id);
        trancheHoraireSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trancheHoraire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/tranche-horaires?query=:query : search for the trancheHoraire corresponding
     * to the query.
     *
     * @param query the query of the trancheHoraire search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/tranche-horaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TrancheHoraire> searchTrancheHoraires(@RequestParam String query) {
        log.debug("REST request to search TrancheHoraires for query {}", query);
        return StreamSupport
            .stream(trancheHoraireSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
