package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Eleve;
import com.afrologix.skulman.repository.EleveRepository;
import com.afrologix.skulman.repository.search.EleveSearchRepository;
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
 * REST controller for managing Eleve.
 */
@RestController
@RequestMapping("/api")
public class EleveResource {

    private final Logger log = LoggerFactory.getLogger(EleveResource.class);
        
    @Inject
    private EleveRepository eleveRepository;
    
    @Inject
    private EleveSearchRepository eleveSearchRepository;
    
    /**
     * POST  /eleves : Create a new eleve.
     *
     * @param eleve the eleve to create
     * @return the ResponseEntity with status 201 (Created) and with body the new eleve, or with status 400 (Bad Request) if the eleve has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/eleves",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Eleve> createEleve(@Valid @RequestBody Eleve eleve) throws URISyntaxException {
        log.debug("REST request to save Eleve : {}", eleve);
        if (eleve.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("eleve", "idexists", "A new eleve cannot already have an ID")).body(null);
        }
        Eleve result = eleveRepository.save(eleve);
        eleveSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/eleves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("eleve", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /eleves : Updates an existing eleve.
     *
     * @param eleve the eleve to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated eleve,
     * or with status 400 (Bad Request) if the eleve is not valid,
     * or with status 500 (Internal Server Error) if the eleve couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/eleves",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Eleve> updateEleve(@Valid @RequestBody Eleve eleve) throws URISyntaxException {
        log.debug("REST request to update Eleve : {}", eleve);
        if (eleve.getId() == null) {
            return createEleve(eleve);
        }
        Eleve result = eleveRepository.save(eleve);
        eleveSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("eleve", eleve.getId().toString()))
            .body(result);
    }

    /**
     * GET  /eleves : get all the eleves.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of eleves in body
     */
    @RequestMapping(value = "/eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Eleve> getAllEleves() {
        log.debug("REST request to get all Eleves");
        List<Eleve> eleves = eleveRepository.findAll();
        return eleves;
    }

    /**
     * GET  /eleves/:id : get the "id" eleve.
     *
     * @param id the id of the eleve to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eleve, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/eleves/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Eleve> getEleve(@PathVariable Long id) {
        log.debug("REST request to get Eleve : {}", id);
        Eleve eleve = eleveRepository.findOne(id);
        return Optional.ofNullable(eleve)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /eleves/:id : delete the "id" eleve.
     *
     * @param id the id of the eleve to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/eleves/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEleve(@PathVariable Long id) {
        log.debug("REST request to delete Eleve : {}", id);
        eleveRepository.delete(id);
        eleveSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("eleve", id.toString())).build();
    }

    /**
     * SEARCH  /_search/eleves?query=:query : search for the eleve corresponding
     * to the query.
     *
     * @param query the query of the eleve search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Eleve> searchEleves(@RequestParam String query) {
        log.debug("REST request to search Eleves for query {}", query);
        return StreamSupport
            .stream(eleveSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
