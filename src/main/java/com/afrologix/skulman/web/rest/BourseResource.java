package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Bourse;
import com.afrologix.skulman.repository.BourseRepository;
import com.afrologix.skulman.repository.search.BourseSearchRepository;
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
 * REST controller for managing Bourse.
 */
@RestController
@RequestMapping("/api")
public class BourseResource {

    private final Logger log = LoggerFactory.getLogger(BourseResource.class);
        
    @Inject
    private BourseRepository bourseRepository;
    
    @Inject
    private BourseSearchRepository bourseSearchRepository;
    
    /**
     * POST  /bourses : Create a new bourse.
     *
     * @param bourse the bourse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bourse, or with status 400 (Bad Request) if the bourse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bourses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bourse> createBourse(@Valid @RequestBody Bourse bourse) throws URISyntaxException {
        log.debug("REST request to save Bourse : {}", bourse);
        if (bourse.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bourse", "idexists", "A new bourse cannot already have an ID")).body(null);
        }
        Bourse result = bourseRepository.save(bourse);
        bourseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bourses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bourse", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bourses : Updates an existing bourse.
     *
     * @param bourse the bourse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bourse,
     * or with status 400 (Bad Request) if the bourse is not valid,
     * or with status 500 (Internal Server Error) if the bourse couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bourses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bourse> updateBourse(@Valid @RequestBody Bourse bourse) throws URISyntaxException {
        log.debug("REST request to update Bourse : {}", bourse);
        if (bourse.getId() == null) {
            return createBourse(bourse);
        }
        Bourse result = bourseRepository.save(bourse);
        bourseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bourse", bourse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bourses : get all the bourses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bourses in body
     */
    @RequestMapping(value = "/bourses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Bourse> getAllBourses() {
        log.debug("REST request to get all Bourses");
        List<Bourse> bourses = bourseRepository.findAll();
        return bourses;
    }

    /**
     * GET  /bourses/:id : get the "id" bourse.
     *
     * @param id the id of the bourse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bourse, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bourses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bourse> getBourse(@PathVariable Long id) {
        log.debug("REST request to get Bourse : {}", id);
        Bourse bourse = bourseRepository.findOne(id);
        return Optional.ofNullable(bourse)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bourses/:id : delete the "id" bourse.
     *
     * @param id the id of the bourse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bourses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBourse(@PathVariable Long id) {
        log.debug("REST request to delete Bourse : {}", id);
        bourseRepository.delete(id);
        bourseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bourse", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bourses?query=:query : search for the bourse corresponding
     * to the query.
     *
     * @param query the query of the bourse search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/bourses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Bourse> searchBourses(@RequestParam String query) {
        log.debug("REST request to search Bourses for query {}", query);
        return StreamSupport
            .stream(bourseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
