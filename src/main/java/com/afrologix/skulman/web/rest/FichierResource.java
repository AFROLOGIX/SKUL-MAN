package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Fichier;
import com.afrologix.skulman.repository.FichierRepository;
import com.afrologix.skulman.repository.search.FichierSearchRepository;
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
 * REST controller for managing Fichier.
 */
@RestController
@RequestMapping("/api")
public class FichierResource {

    private final Logger log = LoggerFactory.getLogger(FichierResource.class);
        
    @Inject
    private FichierRepository fichierRepository;
    
    @Inject
    private FichierSearchRepository fichierSearchRepository;
    
    /**
     * POST  /fichiers : Create a new fichier.
     *
     * @param fichier the fichier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fichier, or with status 400 (Bad Request) if the fichier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fichiers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fichier> createFichier(@Valid @RequestBody Fichier fichier) throws URISyntaxException {
        log.debug("REST request to save Fichier : {}", fichier);
        if (fichier.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fichier", "idexists", "A new fichier cannot already have an ID")).body(null);
        }
        Fichier result = fichierRepository.save(fichier);
        fichierSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fichiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fichier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fichiers : Updates an existing fichier.
     *
     * @param fichier the fichier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fichier,
     * or with status 400 (Bad Request) if the fichier is not valid,
     * or with status 500 (Internal Server Error) if the fichier couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fichiers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fichier> updateFichier(@Valid @RequestBody Fichier fichier) throws URISyntaxException {
        log.debug("REST request to update Fichier : {}", fichier);
        if (fichier.getId() == null) {
            return createFichier(fichier);
        }
        Fichier result = fichierRepository.save(fichier);
        fichierSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fichier", fichier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fichiers : get all the fichiers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fichiers in body
     */
    @RequestMapping(value = "/fichiers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Fichier> getAllFichiers() {
        log.debug("REST request to get all Fichiers");
        List<Fichier> fichiers = fichierRepository.findAll();
        return fichiers;
    }

    /**
     * GET  /fichiers/:id : get the "id" fichier.
     *
     * @param id the id of the fichier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fichier, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/fichiers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fichier> getFichier(@PathVariable Long id) {
        log.debug("REST request to get Fichier : {}", id);
        Fichier fichier = fichierRepository.findOne(id);
        return Optional.ofNullable(fichier)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fichiers/:id : delete the "id" fichier.
     *
     * @param id the id of the fichier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/fichiers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFichier(@PathVariable Long id) {
        log.debug("REST request to delete Fichier : {}", id);
        fichierRepository.delete(id);
        fichierSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fichier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fichiers?query=:query : search for the fichier corresponding
     * to the query.
     *
     * @param query the query of the fichier search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/fichiers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Fichier> searchFichiers(@RequestParam String query) {
        log.debug("REST request to search Fichiers for query {}", query);
        return StreamSupport
            .stream(fichierSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
