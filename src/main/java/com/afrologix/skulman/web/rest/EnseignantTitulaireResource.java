package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.EnseignantTitulaire;
import com.afrologix.skulman.repository.EnseignantTitulaireRepository;
import com.afrologix.skulman.repository.search.EnseignantTitulaireSearchRepository;
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
 * REST controller for managing EnseignantTitulaire.
 */
@RestController
@RequestMapping("/api")
public class EnseignantTitulaireResource {

    private final Logger log = LoggerFactory.getLogger(EnseignantTitulaireResource.class);
        
    @Inject
    private EnseignantTitulaireRepository enseignantTitulaireRepository;
    
    @Inject
    private EnseignantTitulaireSearchRepository enseignantTitulaireSearchRepository;
    
    /**
     * POST  /enseignant-titulaires : Create a new enseignantTitulaire.
     *
     * @param enseignantTitulaire the enseignantTitulaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new enseignantTitulaire, or with status 400 (Bad Request) if the enseignantTitulaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enseignant-titulaires",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EnseignantTitulaire> createEnseignantTitulaire(@Valid @RequestBody EnseignantTitulaire enseignantTitulaire) throws URISyntaxException {
        log.debug("REST request to save EnseignantTitulaire : {}", enseignantTitulaire);
        if (enseignantTitulaire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("enseignantTitulaire", "idexists", "A new enseignantTitulaire cannot already have an ID")).body(null);
        }
        EnseignantTitulaire result = enseignantTitulaireRepository.save(enseignantTitulaire);
        enseignantTitulaireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/enseignant-titulaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("enseignantTitulaire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enseignant-titulaires : Updates an existing enseignantTitulaire.
     *
     * @param enseignantTitulaire the enseignantTitulaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated enseignantTitulaire,
     * or with status 400 (Bad Request) if the enseignantTitulaire is not valid,
     * or with status 500 (Internal Server Error) if the enseignantTitulaire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enseignant-titulaires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EnseignantTitulaire> updateEnseignantTitulaire(@Valid @RequestBody EnseignantTitulaire enseignantTitulaire) throws URISyntaxException {
        log.debug("REST request to update EnseignantTitulaire : {}", enseignantTitulaire);
        if (enseignantTitulaire.getId() == null) {
            return createEnseignantTitulaire(enseignantTitulaire);
        }
        EnseignantTitulaire result = enseignantTitulaireRepository.save(enseignantTitulaire);
        enseignantTitulaireSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("enseignantTitulaire", enseignantTitulaire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enseignant-titulaires : get all the enseignantTitulaires.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of enseignantTitulaires in body
     */
    @RequestMapping(value = "/enseignant-titulaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EnseignantTitulaire> getAllEnseignantTitulaires() {
        log.debug("REST request to get all EnseignantTitulaires");
        List<EnseignantTitulaire> enseignantTitulaires = enseignantTitulaireRepository.findAll();
        return enseignantTitulaires;
    }

    /**
     * GET  /enseignant-titulaires/:id : get the "id" enseignantTitulaire.
     *
     * @param id the id of the enseignantTitulaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the enseignantTitulaire, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/enseignant-titulaires/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EnseignantTitulaire> getEnseignantTitulaire(@PathVariable Long id) {
        log.debug("REST request to get EnseignantTitulaire : {}", id);
        EnseignantTitulaire enseignantTitulaire = enseignantTitulaireRepository.findOne(id);
        return Optional.ofNullable(enseignantTitulaire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /enseignant-titulaires/:id : delete the "id" enseignantTitulaire.
     *
     * @param id the id of the enseignantTitulaire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/enseignant-titulaires/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEnseignantTitulaire(@PathVariable Long id) {
        log.debug("REST request to delete EnseignantTitulaire : {}", id);
        enseignantTitulaireRepository.delete(id);
        enseignantTitulaireSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("enseignantTitulaire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/enseignant-titulaires?query=:query : search for the enseignantTitulaire corresponding
     * to the query.
     *
     * @param query the query of the enseignantTitulaire search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/enseignant-titulaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EnseignantTitulaire> searchEnseignantTitulaires(@RequestParam String query) {
        log.debug("REST request to search EnseignantTitulaires for query {}", query);
        return StreamSupport
            .stream(enseignantTitulaireSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
