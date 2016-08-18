package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Batiment;
import com.afrologix.skulman.repository.BatimentRepository;
import com.afrologix.skulman.repository.search.BatimentSearchRepository;
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
 * REST controller for managing Batiment.
 */
@RestController
@RequestMapping("/api")
public class BatimentResource {

    private final Logger log = LoggerFactory.getLogger(BatimentResource.class);
        
    @Inject
    private BatimentRepository batimentRepository;
    
    @Inject
    private BatimentSearchRepository batimentSearchRepository;
    
    /**
     * POST  /batiments : Create a new batiment.
     *
     * @param batiment the batiment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new batiment, or with status 400 (Bad Request) if the batiment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/batiments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Batiment> createBatiment(@Valid @RequestBody Batiment batiment) throws URISyntaxException {
        log.debug("REST request to save Batiment : {}", batiment);
        if (batiment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("batiment", "idexists", "A new batiment cannot already have an ID")).body(null);
        }
        Batiment result = batimentRepository.save(batiment);
        batimentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/batiments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("batiment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /batiments : Updates an existing batiment.
     *
     * @param batiment the batiment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated batiment,
     * or with status 400 (Bad Request) if the batiment is not valid,
     * or with status 500 (Internal Server Error) if the batiment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/batiments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Batiment> updateBatiment(@Valid @RequestBody Batiment batiment) throws URISyntaxException {
        log.debug("REST request to update Batiment : {}", batiment);
        if (batiment.getId() == null) {
            return createBatiment(batiment);
        }
        Batiment result = batimentRepository.save(batiment);
        batimentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("batiment", batiment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /batiments : get all the batiments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of batiments in body
     */
    @RequestMapping(value = "/batiments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Batiment> getAllBatiments() {
        log.debug("REST request to get all Batiments");
        List<Batiment> batiments = batimentRepository.findAll();
        return batiments;
    }

    /**
     * GET  /batiments/:id : get the "id" batiment.
     *
     * @param id the id of the batiment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the batiment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/batiments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Batiment> getBatiment(@PathVariable Long id) {
        log.debug("REST request to get Batiment : {}", id);
        Batiment batiment = batimentRepository.findOne(id);
        return Optional.ofNullable(batiment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /batiments/:id : delete the "id" batiment.
     *
     * @param id the id of the batiment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/batiments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBatiment(@PathVariable Long id) {
        log.debug("REST request to delete Batiment : {}", id);
        batimentRepository.delete(id);
        batimentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("batiment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/batiments?query=:query : search for the batiment corresponding
     * to the query.
     *
     * @param query the query of the batiment search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/batiments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Batiment> searchBatiments(@RequestParam String query) {
        log.debug("REST request to search Batiments for query {}", query);
        return StreamSupport
            .stream(batimentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
