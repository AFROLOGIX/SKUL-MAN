package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Epreuve;
import com.afrologix.skulman.repository.EpreuveRepository;
import com.afrologix.skulman.repository.search.EpreuveSearchRepository;
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
 * REST controller for managing Epreuve.
 */
@RestController
@RequestMapping("/api")
public class EpreuveResource {

    private final Logger log = LoggerFactory.getLogger(EpreuveResource.class);
        
    @Inject
    private EpreuveRepository epreuveRepository;
    
    @Inject
    private EpreuveSearchRepository epreuveSearchRepository;
    
    /**
     * POST  /epreuves : Create a new epreuve.
     *
     * @param epreuve the epreuve to create
     * @return the ResponseEntity with status 201 (Created) and with body the new epreuve, or with status 400 (Bad Request) if the epreuve has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/epreuves",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Epreuve> createEpreuve(@Valid @RequestBody Epreuve epreuve) throws URISyntaxException {
        log.debug("REST request to save Epreuve : {}", epreuve);
        if (epreuve.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("epreuve", "idexists", "A new epreuve cannot already have an ID")).body(null);
        }
        Epreuve result = epreuveRepository.save(epreuve);
        epreuveSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/epreuves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("epreuve", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /epreuves : Updates an existing epreuve.
     *
     * @param epreuve the epreuve to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated epreuve,
     * or with status 400 (Bad Request) if the epreuve is not valid,
     * or with status 500 (Internal Server Error) if the epreuve couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/epreuves",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Epreuve> updateEpreuve(@Valid @RequestBody Epreuve epreuve) throws URISyntaxException {
        log.debug("REST request to update Epreuve : {}", epreuve);
        if (epreuve.getId() == null) {
            return createEpreuve(epreuve);
        }
        Epreuve result = epreuveRepository.save(epreuve);
        epreuveSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("epreuve", epreuve.getId().toString()))
            .body(result);
    }

    /**
     * GET  /epreuves : get all the epreuves.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of epreuves in body
     */
    @RequestMapping(value = "/epreuves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Epreuve> getAllEpreuves() {
        log.debug("REST request to get all Epreuves");
        List<Epreuve> epreuves = epreuveRepository.findAll();
        return epreuves;
    }

    /**
     * GET  /epreuves/:id : get the "id" epreuve.
     *
     * @param id the id of the epreuve to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the epreuve, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/epreuves/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Epreuve> getEpreuve(@PathVariable Long id) {
        log.debug("REST request to get Epreuve : {}", id);
        Epreuve epreuve = epreuveRepository.findOne(id);
        return Optional.ofNullable(epreuve)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /epreuves/:id : delete the "id" epreuve.
     *
     * @param id the id of the epreuve to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/epreuves/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEpreuve(@PathVariable Long id) {
        log.debug("REST request to delete Epreuve : {}", id);
        epreuveRepository.delete(id);
        epreuveSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("epreuve", id.toString())).build();
    }

    /**
     * SEARCH  /_search/epreuves?query=:query : search for the epreuve corresponding
     * to the query.
     *
     * @param query the query of the epreuve search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/epreuves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Epreuve> searchEpreuves(@RequestParam String query) {
        log.debug("REST request to search Epreuves for query {}", query);
        return StreamSupport
            .stream(epreuveSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
