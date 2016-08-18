package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Statut;
import com.afrologix.skulman.repository.StatutRepository;
import com.afrologix.skulman.repository.search.StatutSearchRepository;
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
 * REST controller for managing Statut.
 */
@RestController
@RequestMapping("/api")
public class StatutResource {

    private final Logger log = LoggerFactory.getLogger(StatutResource.class);
        
    @Inject
    private StatutRepository statutRepository;
    
    @Inject
    private StatutSearchRepository statutSearchRepository;
    
    /**
     * POST  /statuts : Create a new statut.
     *
     * @param statut the statut to create
     * @return the ResponseEntity with status 201 (Created) and with body the new statut, or with status 400 (Bad Request) if the statut has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/statuts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Statut> createStatut(@Valid @RequestBody Statut statut) throws URISyntaxException {
        log.debug("REST request to save Statut : {}", statut);
        if (statut.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("statut", "idexists", "A new statut cannot already have an ID")).body(null);
        }
        Statut result = statutRepository.save(statut);
        statutSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/statuts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("statut", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /statuts : Updates an existing statut.
     *
     * @param statut the statut to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated statut,
     * or with status 400 (Bad Request) if the statut is not valid,
     * or with status 500 (Internal Server Error) if the statut couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/statuts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Statut> updateStatut(@Valid @RequestBody Statut statut) throws URISyntaxException {
        log.debug("REST request to update Statut : {}", statut);
        if (statut.getId() == null) {
            return createStatut(statut);
        }
        Statut result = statutRepository.save(statut);
        statutSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("statut", statut.getId().toString()))
            .body(result);
    }

    /**
     * GET  /statuts : get all the statuts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of statuts in body
     */
    @RequestMapping(value = "/statuts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Statut> getAllStatuts() {
        log.debug("REST request to get all Statuts");
        List<Statut> statuts = statutRepository.findAll();
        return statuts;
    }

    /**
     * GET  /statuts/:id : get the "id" statut.
     *
     * @param id the id of the statut to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the statut, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/statuts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Statut> getStatut(@PathVariable Long id) {
        log.debug("REST request to get Statut : {}", id);
        Statut statut = statutRepository.findOne(id);
        return Optional.ofNullable(statut)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /statuts/:id : delete the "id" statut.
     *
     * @param id the id of the statut to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/statuts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStatut(@PathVariable Long id) {
        log.debug("REST request to delete Statut : {}", id);
        statutRepository.delete(id);
        statutSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("statut", id.toString())).build();
    }

    /**
     * SEARCH  /_search/statuts?query=:query : search for the statut corresponding
     * to the query.
     *
     * @param query the query of the statut search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/statuts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Statut> searchStatuts(@RequestParam String query) {
        log.debug("REST request to search Statuts for query {}", query);
        return StreamSupport
            .stream(statutSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
