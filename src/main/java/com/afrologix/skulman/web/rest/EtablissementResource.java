package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Etablissement;
import com.afrologix.skulman.repository.EtablissementRepository;
import com.afrologix.skulman.repository.search.EtablissementSearchRepository;
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
 * REST controller for managing Etablissement.
 */
@RestController
@RequestMapping("/api")
public class EtablissementResource {

    private final Logger log = LoggerFactory.getLogger(EtablissementResource.class);
        
    @Inject
    private EtablissementRepository etablissementRepository;
    
    @Inject
    private EtablissementSearchRepository etablissementSearchRepository;
    
    /**
     * POST  /etablissements : Create a new etablissement.
     *
     * @param etablissement the etablissement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new etablissement, or with status 400 (Bad Request) if the etablissement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/etablissements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Etablissement> createEtablissement(@Valid @RequestBody Etablissement etablissement) throws URISyntaxException {
        log.debug("REST request to save Etablissement : {}", etablissement);
        if (etablissement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("etablissement", "idexists", "A new etablissement cannot already have an ID")).body(null);
        }
        Etablissement result = etablissementRepository.save(etablissement);
        etablissementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/etablissements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("etablissement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /etablissements : Updates an existing etablissement.
     *
     * @param etablissement the etablissement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated etablissement,
     * or with status 400 (Bad Request) if the etablissement is not valid,
     * or with status 500 (Internal Server Error) if the etablissement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/etablissements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Etablissement> updateEtablissement(@Valid @RequestBody Etablissement etablissement) throws URISyntaxException {
        log.debug("REST request to update Etablissement : {}", etablissement);
        if (etablissement.getId() == null) {
            return createEtablissement(etablissement);
        }
        Etablissement result = etablissementRepository.save(etablissement);
        etablissementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("etablissement", etablissement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /etablissements : get all the etablissements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of etablissements in body
     */
    @RequestMapping(value = "/etablissements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Etablissement> getAllEtablissements() {
        log.debug("REST request to get all Etablissements");
        List<Etablissement> etablissements = etablissementRepository.findAll();
        return etablissements;
    }

    /**
     * GET  /etablissements/:id : get the "id" etablissement.
     *
     * @param id the id of the etablissement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the etablissement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/etablissements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Etablissement> getEtablissement(@PathVariable Long id) {
        log.debug("REST request to get Etablissement : {}", id);
        Etablissement etablissement = etablissementRepository.findOne(id);
        return Optional.ofNullable(etablissement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /etablissements/:id : delete the "id" etablissement.
     *
     * @param id the id of the etablissement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/etablissements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEtablissement(@PathVariable Long id) {
        log.debug("REST request to delete Etablissement : {}", id);
        etablissementRepository.delete(id);
        etablissementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("etablissement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/etablissements?query=:query : search for the etablissement corresponding
     * to the query.
     *
     * @param query the query of the etablissement search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/etablissements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Etablissement> searchEtablissements(@RequestParam String query) {
        log.debug("REST request to search Etablissements for query {}", query);
        return StreamSupport
            .stream(etablissementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
