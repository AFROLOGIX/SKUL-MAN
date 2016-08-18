package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Cours;
import com.afrologix.skulman.repository.CoursRepository;
import com.afrologix.skulman.repository.search.CoursSearchRepository;
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
 * REST controller for managing Cours.
 */
@RestController
@RequestMapping("/api")
public class CoursResource {

    private final Logger log = LoggerFactory.getLogger(CoursResource.class);
        
    @Inject
    private CoursRepository coursRepository;
    
    @Inject
    private CoursSearchRepository coursSearchRepository;
    
    /**
     * POST  /cours : Create a new cours.
     *
     * @param cours the cours to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cours, or with status 400 (Bad Request) if the cours has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cours",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cours> createCours(@Valid @RequestBody Cours cours) throws URISyntaxException {
        log.debug("REST request to save Cours : {}", cours);
        if (cours.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cours", "idexists", "A new cours cannot already have an ID")).body(null);
        }
        Cours result = coursRepository.save(cours);
        coursSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cours", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cours : Updates an existing cours.
     *
     * @param cours the cours to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cours,
     * or with status 400 (Bad Request) if the cours is not valid,
     * or with status 500 (Internal Server Error) if the cours couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cours",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cours> updateCours(@Valid @RequestBody Cours cours) throws URISyntaxException {
        log.debug("REST request to update Cours : {}", cours);
        if (cours.getId() == null) {
            return createCours(cours);
        }
        Cours result = coursRepository.save(cours);
        coursSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cours", cours.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cours : get all the cours.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cours in body
     */
    @RequestMapping(value = "/cours",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cours> getAllCours() {
        log.debug("REST request to get all Cours");
        List<Cours> cours = coursRepository.findAll();
        return cours;
    }

    /**
     * GET  /cours/:id : get the "id" cours.
     *
     * @param id the id of the cours to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cours, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cours/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cours> getCours(@PathVariable Long id) {
        log.debug("REST request to get Cours : {}", id);
        Cours cours = coursRepository.findOne(id);
        return Optional.ofNullable(cours)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cours/:id : delete the "id" cours.
     *
     * @param id the id of the cours to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cours/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        log.debug("REST request to delete Cours : {}", id);
        coursRepository.delete(id);
        coursSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cours", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cours?query=:query : search for the cours corresponding
     * to the query.
     *
     * @param query the query of the cours search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/cours",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cours> searchCours(@RequestParam String query) {
        log.debug("REST request to search Cours for query {}", query);
        return StreamSupport
            .stream(coursSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
