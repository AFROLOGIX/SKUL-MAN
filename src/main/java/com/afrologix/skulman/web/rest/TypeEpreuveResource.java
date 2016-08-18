package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.TypeEpreuve;
import com.afrologix.skulman.repository.TypeEpreuveRepository;
import com.afrologix.skulman.repository.search.TypeEpreuveSearchRepository;
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
 * REST controller for managing TypeEpreuve.
 */
@RestController
@RequestMapping("/api")
public class TypeEpreuveResource {

    private final Logger log = LoggerFactory.getLogger(TypeEpreuveResource.class);
        
    @Inject
    private TypeEpreuveRepository typeEpreuveRepository;
    
    @Inject
    private TypeEpreuveSearchRepository typeEpreuveSearchRepository;
    
    /**
     * POST  /type-epreuves : Create a new typeEpreuve.
     *
     * @param typeEpreuve the typeEpreuve to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeEpreuve, or with status 400 (Bad Request) if the typeEpreuve has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-epreuves",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeEpreuve> createTypeEpreuve(@Valid @RequestBody TypeEpreuve typeEpreuve) throws URISyntaxException {
        log.debug("REST request to save TypeEpreuve : {}", typeEpreuve);
        if (typeEpreuve.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeEpreuve", "idexists", "A new typeEpreuve cannot already have an ID")).body(null);
        }
        TypeEpreuve result = typeEpreuveRepository.save(typeEpreuve);
        typeEpreuveSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-epreuves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeEpreuve", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-epreuves : Updates an existing typeEpreuve.
     *
     * @param typeEpreuve the typeEpreuve to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeEpreuve,
     * or with status 400 (Bad Request) if the typeEpreuve is not valid,
     * or with status 500 (Internal Server Error) if the typeEpreuve couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-epreuves",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeEpreuve> updateTypeEpreuve(@Valid @RequestBody TypeEpreuve typeEpreuve) throws URISyntaxException {
        log.debug("REST request to update TypeEpreuve : {}", typeEpreuve);
        if (typeEpreuve.getId() == null) {
            return createTypeEpreuve(typeEpreuve);
        }
        TypeEpreuve result = typeEpreuveRepository.save(typeEpreuve);
        typeEpreuveSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeEpreuve", typeEpreuve.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-epreuves : get all the typeEpreuves.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeEpreuves in body
     */
    @RequestMapping(value = "/type-epreuves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeEpreuve> getAllTypeEpreuves() {
        log.debug("REST request to get all TypeEpreuves");
        List<TypeEpreuve> typeEpreuves = typeEpreuveRepository.findAll();
        return typeEpreuves;
    }

    /**
     * GET  /type-epreuves/:id : get the "id" typeEpreuve.
     *
     * @param id the id of the typeEpreuve to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeEpreuve, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-epreuves/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeEpreuve> getTypeEpreuve(@PathVariable Long id) {
        log.debug("REST request to get TypeEpreuve : {}", id);
        TypeEpreuve typeEpreuve = typeEpreuveRepository.findOne(id);
        return Optional.ofNullable(typeEpreuve)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-epreuves/:id : delete the "id" typeEpreuve.
     *
     * @param id the id of the typeEpreuve to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-epreuves/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypeEpreuve(@PathVariable Long id) {
        log.debug("REST request to delete TypeEpreuve : {}", id);
        typeEpreuveRepository.delete(id);
        typeEpreuveSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeEpreuve", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-epreuves?query=:query : search for the typeEpreuve corresponding
     * to the query.
     *
     * @param query the query of the typeEpreuve search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-epreuves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeEpreuve> searchTypeEpreuves(@RequestParam String query) {
        log.debug("REST request to search TypeEpreuves for query {}", query);
        return StreamSupport
            .stream(typeEpreuveSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
