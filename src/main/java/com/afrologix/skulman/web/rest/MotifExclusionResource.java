package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.MotifExclusion;
import com.afrologix.skulman.repository.MotifExclusionRepository;
import com.afrologix.skulman.repository.search.MotifExclusionSearchRepository;
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
 * REST controller for managing MotifExclusion.
 */
@RestController
@RequestMapping("/api")
public class MotifExclusionResource {

    private final Logger log = LoggerFactory.getLogger(MotifExclusionResource.class);
        
    @Inject
    private MotifExclusionRepository motifExclusionRepository;
    
    @Inject
    private MotifExclusionSearchRepository motifExclusionSearchRepository;
    
    /**
     * POST  /motif-exclusions : Create a new motifExclusion.
     *
     * @param motifExclusion the motifExclusion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new motifExclusion, or with status 400 (Bad Request) if the motifExclusion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/motif-exclusions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MotifExclusion> createMotifExclusion(@Valid @RequestBody MotifExclusion motifExclusion) throws URISyntaxException {
        log.debug("REST request to save MotifExclusion : {}", motifExclusion);
        if (motifExclusion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("motifExclusion", "idexists", "A new motifExclusion cannot already have an ID")).body(null);
        }
        MotifExclusion result = motifExclusionRepository.save(motifExclusion);
        motifExclusionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/motif-exclusions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("motifExclusion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /motif-exclusions : Updates an existing motifExclusion.
     *
     * @param motifExclusion the motifExclusion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated motifExclusion,
     * or with status 400 (Bad Request) if the motifExclusion is not valid,
     * or with status 500 (Internal Server Error) if the motifExclusion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/motif-exclusions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MotifExclusion> updateMotifExclusion(@Valid @RequestBody MotifExclusion motifExclusion) throws URISyntaxException {
        log.debug("REST request to update MotifExclusion : {}", motifExclusion);
        if (motifExclusion.getId() == null) {
            return createMotifExclusion(motifExclusion);
        }
        MotifExclusion result = motifExclusionRepository.save(motifExclusion);
        motifExclusionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("motifExclusion", motifExclusion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /motif-exclusions : get all the motifExclusions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of motifExclusions in body
     */
    @RequestMapping(value = "/motif-exclusions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MotifExclusion> getAllMotifExclusions() {
        log.debug("REST request to get all MotifExclusions");
        List<MotifExclusion> motifExclusions = motifExclusionRepository.findAll();
        return motifExclusions;
    }

    /**
     * GET  /motif-exclusions/:id : get the "id" motifExclusion.
     *
     * @param id the id of the motifExclusion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the motifExclusion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/motif-exclusions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MotifExclusion> getMotifExclusion(@PathVariable Long id) {
        log.debug("REST request to get MotifExclusion : {}", id);
        MotifExclusion motifExclusion = motifExclusionRepository.findOne(id);
        return Optional.ofNullable(motifExclusion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /motif-exclusions/:id : delete the "id" motifExclusion.
     *
     * @param id the id of the motifExclusion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/motif-exclusions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMotifExclusion(@PathVariable Long id) {
        log.debug("REST request to delete MotifExclusion : {}", id);
        motifExclusionRepository.delete(id);
        motifExclusionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("motifExclusion", id.toString())).build();
    }

    /**
     * SEARCH  /_search/motif-exclusions?query=:query : search for the motifExclusion corresponding
     * to the query.
     *
     * @param query the query of the motifExclusion search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/motif-exclusions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MotifExclusion> searchMotifExclusions(@RequestParam String query) {
        log.debug("REST request to search MotifExclusions for query {}", query);
        return StreamSupport
            .stream(motifExclusionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
