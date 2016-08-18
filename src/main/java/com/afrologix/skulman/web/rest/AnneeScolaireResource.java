package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.AnneeScolaire;
import com.afrologix.skulman.repository.AnneeScolaireRepository;
import com.afrologix.skulman.repository.search.AnneeScolaireSearchRepository;
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
 * REST controller for managing AnneeScolaire.
 */
@RestController
@RequestMapping("/api")
public class AnneeScolaireResource {

    private final Logger log = LoggerFactory.getLogger(AnneeScolaireResource.class);
        
    @Inject
    private AnneeScolaireRepository anneeScolaireRepository;
    
    @Inject
    private AnneeScolaireSearchRepository anneeScolaireSearchRepository;
    
    /**
     * POST  /annee-scolaires : Create a new anneeScolaire.
     *
     * @param anneeScolaire the anneeScolaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new anneeScolaire, or with status 400 (Bad Request) if the anneeScolaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/annee-scolaires",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AnneeScolaire> createAnneeScolaire(@Valid @RequestBody AnneeScolaire anneeScolaire) throws URISyntaxException {
        log.debug("REST request to save AnneeScolaire : {}", anneeScolaire);
        if (anneeScolaire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("anneeScolaire", "idexists", "A new anneeScolaire cannot already have an ID")).body(null);
        }
        AnneeScolaire result = anneeScolaireRepository.save(anneeScolaire);
        anneeScolaireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/annee-scolaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("anneeScolaire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /annee-scolaires : Updates an existing anneeScolaire.
     *
     * @param anneeScolaire the anneeScolaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated anneeScolaire,
     * or with status 400 (Bad Request) if the anneeScolaire is not valid,
     * or with status 500 (Internal Server Error) if the anneeScolaire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/annee-scolaires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AnneeScolaire> updateAnneeScolaire(@Valid @RequestBody AnneeScolaire anneeScolaire) throws URISyntaxException {
        log.debug("REST request to update AnneeScolaire : {}", anneeScolaire);
        if (anneeScolaire.getId() == null) {
            return createAnneeScolaire(anneeScolaire);
        }
        AnneeScolaire result = anneeScolaireRepository.save(anneeScolaire);
        anneeScolaireSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("anneeScolaire", anneeScolaire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /annee-scolaires : get all the anneeScolaires.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anneeScolaires in body
     */
    @RequestMapping(value = "/annee-scolaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AnneeScolaire> getAllAnneeScolaires() {
        log.debug("REST request to get all AnneeScolaires");
        List<AnneeScolaire> anneeScolaires = anneeScolaireRepository.findAll();
        return anneeScolaires;
    }

    /**
     * GET  /annee-scolaires/:id : get the "id" anneeScolaire.
     *
     * @param id the id of the anneeScolaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the anneeScolaire, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/annee-scolaires/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AnneeScolaire> getAnneeScolaire(@PathVariable Long id) {
        log.debug("REST request to get AnneeScolaire : {}", id);
        AnneeScolaire anneeScolaire = anneeScolaireRepository.findOne(id);
        return Optional.ofNullable(anneeScolaire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /annee-scolaires/:id : delete the "id" anneeScolaire.
     *
     * @param id the id of the anneeScolaire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/annee-scolaires/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAnneeScolaire(@PathVariable Long id) {
        log.debug("REST request to delete AnneeScolaire : {}", id);
        anneeScolaireRepository.delete(id);
        anneeScolaireSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("anneeScolaire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/annee-scolaires?query=:query : search for the anneeScolaire corresponding
     * to the query.
     *
     * @param query the query of the anneeScolaire search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/annee-scolaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AnneeScolaire> searchAnneeScolaires(@RequestParam String query) {
        log.debug("REST request to search AnneeScolaires for query {}", query);
        return StreamSupport
            .stream(anneeScolaireSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
