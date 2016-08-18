package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Salle;
import com.afrologix.skulman.repository.SalleRepository;
import com.afrologix.skulman.repository.search.SalleSearchRepository;
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
 * REST controller for managing Salle.
 */
@RestController
@RequestMapping("/api")
public class SalleResource {

    private final Logger log = LoggerFactory.getLogger(SalleResource.class);
        
    @Inject
    private SalleRepository salleRepository;
    
    @Inject
    private SalleSearchRepository salleSearchRepository;
    
    /**
     * POST  /salles : Create a new salle.
     *
     * @param salle the salle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salle, or with status 400 (Bad Request) if the salle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/salles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Salle> createSalle(@Valid @RequestBody Salle salle) throws URISyntaxException {
        log.debug("REST request to save Salle : {}", salle);
        if (salle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("salle", "idexists", "A new salle cannot already have an ID")).body(null);
        }
        Salle result = salleRepository.save(salle);
        salleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/salles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("salle", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /salles : Updates an existing salle.
     *
     * @param salle the salle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salle,
     * or with status 400 (Bad Request) if the salle is not valid,
     * or with status 500 (Internal Server Error) if the salle couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/salles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Salle> updateSalle(@Valid @RequestBody Salle salle) throws URISyntaxException {
        log.debug("REST request to update Salle : {}", salle);
        if (salle.getId() == null) {
            return createSalle(salle);
        }
        Salle result = salleRepository.save(salle);
        salleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("salle", salle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /salles : get all the salles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of salles in body
     */
    @RequestMapping(value = "/salles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Salle> getAllSalles() {
        log.debug("REST request to get all Salles");
        List<Salle> salles = salleRepository.findAll();
        return salles;
    }

    /**
     * GET  /salles/:id : get the "id" salle.
     *
     * @param id the id of the salle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salle, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/salles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Salle> getSalle(@PathVariable Long id) {
        log.debug("REST request to get Salle : {}", id);
        Salle salle = salleRepository.findOne(id);
        return Optional.ofNullable(salle)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /salles/:id : delete the "id" salle.
     *
     * @param id the id of the salle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/salles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSalle(@PathVariable Long id) {
        log.debug("REST request to delete Salle : {}", id);
        salleRepository.delete(id);
        salleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("salle", id.toString())).build();
    }

    /**
     * SEARCH  /_search/salles?query=:query : search for the salle corresponding
     * to the query.
     *
     * @param query the query of the salle search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/salles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Salle> searchSalles(@RequestParam String query) {
        log.debug("REST request to search Salles for query {}", query);
        return StreamSupport
            .stream(salleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
