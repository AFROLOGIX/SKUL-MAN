package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.TypeMoratoire;
import com.afrologix.skulman.repository.TypeMoratoireRepository;
import com.afrologix.skulman.repository.search.TypeMoratoireSearchRepository;
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
 * REST controller for managing TypeMoratoire.
 */
@RestController
@RequestMapping("/api")
public class TypeMoratoireResource {

    private final Logger log = LoggerFactory.getLogger(TypeMoratoireResource.class);
        
    @Inject
    private TypeMoratoireRepository typeMoratoireRepository;
    
    @Inject
    private TypeMoratoireSearchRepository typeMoratoireSearchRepository;
    
    /**
     * POST  /type-moratoires : Create a new typeMoratoire.
     *
     * @param typeMoratoire the typeMoratoire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeMoratoire, or with status 400 (Bad Request) if the typeMoratoire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-moratoires",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeMoratoire> createTypeMoratoire(@Valid @RequestBody TypeMoratoire typeMoratoire) throws URISyntaxException {
        log.debug("REST request to save TypeMoratoire : {}", typeMoratoire);
        if (typeMoratoire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeMoratoire", "idexists", "A new typeMoratoire cannot already have an ID")).body(null);
        }
        TypeMoratoire result = typeMoratoireRepository.save(typeMoratoire);
        typeMoratoireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-moratoires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeMoratoire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-moratoires : Updates an existing typeMoratoire.
     *
     * @param typeMoratoire the typeMoratoire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeMoratoire,
     * or with status 400 (Bad Request) if the typeMoratoire is not valid,
     * or with status 500 (Internal Server Error) if the typeMoratoire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-moratoires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeMoratoire> updateTypeMoratoire(@Valid @RequestBody TypeMoratoire typeMoratoire) throws URISyntaxException {
        log.debug("REST request to update TypeMoratoire : {}", typeMoratoire);
        if (typeMoratoire.getId() == null) {
            return createTypeMoratoire(typeMoratoire);
        }
        TypeMoratoire result = typeMoratoireRepository.save(typeMoratoire);
        typeMoratoireSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeMoratoire", typeMoratoire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-moratoires : get all the typeMoratoires.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeMoratoires in body
     */
    @RequestMapping(value = "/type-moratoires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeMoratoire> getAllTypeMoratoires() {
        log.debug("REST request to get all TypeMoratoires");
        List<TypeMoratoire> typeMoratoires = typeMoratoireRepository.findAll();
        return typeMoratoires;
    }

    /**
     * GET  /type-moratoires/:id : get the "id" typeMoratoire.
     *
     * @param id the id of the typeMoratoire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeMoratoire, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-moratoires/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeMoratoire> getTypeMoratoire(@PathVariable Long id) {
        log.debug("REST request to get TypeMoratoire : {}", id);
        TypeMoratoire typeMoratoire = typeMoratoireRepository.findOne(id);
        return Optional.ofNullable(typeMoratoire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-moratoires/:id : delete the "id" typeMoratoire.
     *
     * @param id the id of the typeMoratoire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-moratoires/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypeMoratoire(@PathVariable Long id) {
        log.debug("REST request to delete TypeMoratoire : {}", id);
        typeMoratoireRepository.delete(id);
        typeMoratoireSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeMoratoire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-moratoires?query=:query : search for the typeMoratoire corresponding
     * to the query.
     *
     * @param query the query of the typeMoratoire search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-moratoires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeMoratoire> searchTypeMoratoires(@RequestParam String query) {
        log.debug("REST request to search TypeMoratoires for query {}", query);
        return StreamSupport
            .stream(typeMoratoireSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
