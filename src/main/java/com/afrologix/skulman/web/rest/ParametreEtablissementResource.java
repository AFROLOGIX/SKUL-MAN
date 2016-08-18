package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.ParametreEtablissement;
import com.afrologix.skulman.repository.ParametreEtablissementRepository;
import com.afrologix.skulman.repository.search.ParametreEtablissementSearchRepository;
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
 * REST controller for managing ParametreEtablissement.
 */
@RestController
@RequestMapping("/api")
public class ParametreEtablissementResource {

    private final Logger log = LoggerFactory.getLogger(ParametreEtablissementResource.class);
        
    @Inject
    private ParametreEtablissementRepository parametreEtablissementRepository;
    
    @Inject
    private ParametreEtablissementSearchRepository parametreEtablissementSearchRepository;
    
    /**
     * POST  /parametre-etablissements : Create a new parametreEtablissement.
     *
     * @param parametreEtablissement the parametreEtablissement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new parametreEtablissement, or with status 400 (Bad Request) if the parametreEtablissement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/parametre-etablissements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParametreEtablissement> createParametreEtablissement(@Valid @RequestBody ParametreEtablissement parametreEtablissement) throws URISyntaxException {
        log.debug("REST request to save ParametreEtablissement : {}", parametreEtablissement);
        if (parametreEtablissement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("parametreEtablissement", "idexists", "A new parametreEtablissement cannot already have an ID")).body(null);
        }
        ParametreEtablissement result = parametreEtablissementRepository.save(parametreEtablissement);
        parametreEtablissementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/parametre-etablissements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("parametreEtablissement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /parametre-etablissements : Updates an existing parametreEtablissement.
     *
     * @param parametreEtablissement the parametreEtablissement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated parametreEtablissement,
     * or with status 400 (Bad Request) if the parametreEtablissement is not valid,
     * or with status 500 (Internal Server Error) if the parametreEtablissement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/parametre-etablissements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParametreEtablissement> updateParametreEtablissement(@Valid @RequestBody ParametreEtablissement parametreEtablissement) throws URISyntaxException {
        log.debug("REST request to update ParametreEtablissement : {}", parametreEtablissement);
        if (parametreEtablissement.getId() == null) {
            return createParametreEtablissement(parametreEtablissement);
        }
        ParametreEtablissement result = parametreEtablissementRepository.save(parametreEtablissement);
        parametreEtablissementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("parametreEtablissement", parametreEtablissement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /parametre-etablissements : get all the parametreEtablissements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of parametreEtablissements in body
     */
    @RequestMapping(value = "/parametre-etablissements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ParametreEtablissement> getAllParametreEtablissements() {
        log.debug("REST request to get all ParametreEtablissements");
        List<ParametreEtablissement> parametreEtablissements = parametreEtablissementRepository.findAll();
        return parametreEtablissements;
    }

    /**
     * GET  /parametre-etablissements/:id : get the "id" parametreEtablissement.
     *
     * @param id the id of the parametreEtablissement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the parametreEtablissement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/parametre-etablissements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParametreEtablissement> getParametreEtablissement(@PathVariable Long id) {
        log.debug("REST request to get ParametreEtablissement : {}", id);
        ParametreEtablissement parametreEtablissement = parametreEtablissementRepository.findOne(id);
        return Optional.ofNullable(parametreEtablissement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parametre-etablissements/:id : delete the "id" parametreEtablissement.
     *
     * @param id the id of the parametreEtablissement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/parametre-etablissements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteParametreEtablissement(@PathVariable Long id) {
        log.debug("REST request to delete ParametreEtablissement : {}", id);
        parametreEtablissementRepository.delete(id);
        parametreEtablissementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("parametreEtablissement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/parametre-etablissements?query=:query : search for the parametreEtablissement corresponding
     * to the query.
     *
     * @param query the query of the parametreEtablissement search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/parametre-etablissements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ParametreEtablissement> searchParametreEtablissements(@RequestParam String query) {
        log.debug("REST request to search ParametreEtablissements for query {}", query);
        return StreamSupport
            .stream(parametreEtablissementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
