package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Pause;
import com.afrologix.skulman.repository.PauseRepository;
import com.afrologix.skulman.repository.search.PauseSearchRepository;
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
 * REST controller for managing Pause.
 */
@RestController
@RequestMapping("/api")
public class PauseResource {

    private final Logger log = LoggerFactory.getLogger(PauseResource.class);
        
    @Inject
    private PauseRepository pauseRepository;
    
    @Inject
    private PauseSearchRepository pauseSearchRepository;
    
    /**
     * POST  /pauses : Create a new pause.
     *
     * @param pause the pause to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pause, or with status 400 (Bad Request) if the pause has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pauses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pause> createPause(@Valid @RequestBody Pause pause) throws URISyntaxException {
        log.debug("REST request to save Pause : {}", pause);
        if (pause.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pause", "idexists", "A new pause cannot already have an ID")).body(null);
        }
        Pause result = pauseRepository.save(pause);
        pauseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pauses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pause", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pauses : Updates an existing pause.
     *
     * @param pause the pause to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pause,
     * or with status 400 (Bad Request) if the pause is not valid,
     * or with status 500 (Internal Server Error) if the pause couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pauses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pause> updatePause(@Valid @RequestBody Pause pause) throws URISyntaxException {
        log.debug("REST request to update Pause : {}", pause);
        if (pause.getId() == null) {
            return createPause(pause);
        }
        Pause result = pauseRepository.save(pause);
        pauseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pause", pause.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pauses : get all the pauses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pauses in body
     */
    @RequestMapping(value = "/pauses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pause> getAllPauses() {
        log.debug("REST request to get all Pauses");
        List<Pause> pauses = pauseRepository.findAll();
        return pauses;
    }

    /**
     * GET  /pauses/:id : get the "id" pause.
     *
     * @param id the id of the pause to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pause, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pauses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pause> getPause(@PathVariable Long id) {
        log.debug("REST request to get Pause : {}", id);
        Pause pause = pauseRepository.findOne(id);
        return Optional.ofNullable(pause)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pauses/:id : delete the "id" pause.
     *
     * @param id the id of the pause to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pauses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePause(@PathVariable Long id) {
        log.debug("REST request to delete Pause : {}", id);
        pauseRepository.delete(id);
        pauseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pause", id.toString())).build();
    }

    /**
     * SEARCH  /_search/pauses?query=:query : search for the pause corresponding
     * to the query.
     *
     * @param query the query of the pause search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/pauses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pause> searchPauses(@RequestParam String query) {
        log.debug("REST request to search Pauses for query {}", query);
        return StreamSupport
            .stream(pauseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
