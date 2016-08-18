package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Religion;
import com.afrologix.skulman.repository.ReligionRepository;
import com.afrologix.skulman.repository.search.ReligionSearchRepository;
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
 * REST controller for managing Religion.
 */
@RestController
@RequestMapping("/api")
public class ReligionResource {

    private final Logger log = LoggerFactory.getLogger(ReligionResource.class);
        
    @Inject
    private ReligionRepository religionRepository;
    
    @Inject
    private ReligionSearchRepository religionSearchRepository;
    
    /**
     * POST  /religions : Create a new religion.
     *
     * @param religion the religion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new religion, or with status 400 (Bad Request) if the religion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/religions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Religion> createReligion(@Valid @RequestBody Religion religion) throws URISyntaxException {
        log.debug("REST request to save Religion : {}", religion);
        if (religion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("religion", "idexists", "A new religion cannot already have an ID")).body(null);
        }
        Religion result = religionRepository.save(religion);
        religionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/religions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("religion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /religions : Updates an existing religion.
     *
     * @param religion the religion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated religion,
     * or with status 400 (Bad Request) if the religion is not valid,
     * or with status 500 (Internal Server Error) if the religion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/religions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Religion> updateReligion(@Valid @RequestBody Religion religion) throws URISyntaxException {
        log.debug("REST request to update Religion : {}", religion);
        if (religion.getId() == null) {
            return createReligion(religion);
        }
        Religion result = religionRepository.save(religion);
        religionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("religion", religion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /religions : get all the religions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of religions in body
     */
    @RequestMapping(value = "/religions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Religion> getAllReligions() {
        log.debug("REST request to get all Religions");
        List<Religion> religions = religionRepository.findAll();
        return religions;
    }

    /**
     * GET  /religions/:id : get the "id" religion.
     *
     * @param id the id of the religion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the religion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/religions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Religion> getReligion(@PathVariable Long id) {
        log.debug("REST request to get Religion : {}", id);
        Religion religion = religionRepository.findOne(id);
        return Optional.ofNullable(religion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /religions/:id : delete the "id" religion.
     *
     * @param id the id of the religion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/religions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReligion(@PathVariable Long id) {
        log.debug("REST request to delete Religion : {}", id);
        religionRepository.delete(id);
        religionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("religion", id.toString())).build();
    }

    /**
     * SEARCH  /_search/religions?query=:query : search for the religion corresponding
     * to the query.
     *
     * @param query the query of the religion search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/religions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Religion> searchReligions(@RequestParam String query) {
        log.debug("REST request to search Religions for query {}", query);
        return StreamSupport
            .stream(religionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
