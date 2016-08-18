package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.StatutEleve;
import com.afrologix.skulman.repository.StatutEleveRepository;
import com.afrologix.skulman.repository.search.StatutEleveSearchRepository;
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
 * REST controller for managing StatutEleve.
 */
@RestController
@RequestMapping("/api")
public class StatutEleveResource {

    private final Logger log = LoggerFactory.getLogger(StatutEleveResource.class);
        
    @Inject
    private StatutEleveRepository statutEleveRepository;
    
    @Inject
    private StatutEleveSearchRepository statutEleveSearchRepository;
    
    /**
     * POST  /statut-eleves : Create a new statutEleve.
     *
     * @param statutEleve the statutEleve to create
     * @return the ResponseEntity with status 201 (Created) and with body the new statutEleve, or with status 400 (Bad Request) if the statutEleve has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/statut-eleves",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StatutEleve> createStatutEleve(@Valid @RequestBody StatutEleve statutEleve) throws URISyntaxException {
        log.debug("REST request to save StatutEleve : {}", statutEleve);
        if (statutEleve.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("statutEleve", "idexists", "A new statutEleve cannot already have an ID")).body(null);
        }
        StatutEleve result = statutEleveRepository.save(statutEleve);
        statutEleveSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/statut-eleves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("statutEleve", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /statut-eleves : Updates an existing statutEleve.
     *
     * @param statutEleve the statutEleve to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated statutEleve,
     * or with status 400 (Bad Request) if the statutEleve is not valid,
     * or with status 500 (Internal Server Error) if the statutEleve couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/statut-eleves",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StatutEleve> updateStatutEleve(@Valid @RequestBody StatutEleve statutEleve) throws URISyntaxException {
        log.debug("REST request to update StatutEleve : {}", statutEleve);
        if (statutEleve.getId() == null) {
            return createStatutEleve(statutEleve);
        }
        StatutEleve result = statutEleveRepository.save(statutEleve);
        statutEleveSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("statutEleve", statutEleve.getId().toString()))
            .body(result);
    }

    /**
     * GET  /statut-eleves : get all the statutEleves.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of statutEleves in body
     */
    @RequestMapping(value = "/statut-eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StatutEleve> getAllStatutEleves() {
        log.debug("REST request to get all StatutEleves");
        List<StatutEleve> statutEleves = statutEleveRepository.findAll();
        return statutEleves;
    }

    /**
     * GET  /statut-eleves/:id : get the "id" statutEleve.
     *
     * @param id the id of the statutEleve to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the statutEleve, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/statut-eleves/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StatutEleve> getStatutEleve(@PathVariable Long id) {
        log.debug("REST request to get StatutEleve : {}", id);
        StatutEleve statutEleve = statutEleveRepository.findOne(id);
        return Optional.ofNullable(statutEleve)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /statut-eleves/:id : delete the "id" statutEleve.
     *
     * @param id the id of the statutEleve to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/statut-eleves/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStatutEleve(@PathVariable Long id) {
        log.debug("REST request to delete StatutEleve : {}", id);
        statutEleveRepository.delete(id);
        statutEleveSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("statutEleve", id.toString())).build();
    }

    /**
     * SEARCH  /_search/statut-eleves?query=:query : search for the statutEleve corresponding
     * to the query.
     *
     * @param query the query of the statutEleve search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/statut-eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StatutEleve> searchStatutEleves(@RequestParam String query) {
        log.debug("REST request to search StatutEleves for query {}", query);
        return StreamSupport
            .stream(statutEleveSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
