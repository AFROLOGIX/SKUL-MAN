package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.TypePersonnel;
import com.afrologix.skulman.repository.TypePersonnelRepository;
import com.afrologix.skulman.repository.search.TypePersonnelSearchRepository;
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
 * REST controller for managing TypePersonnel.
 */
@RestController
@RequestMapping("/api")
public class TypePersonnelResource {

    private final Logger log = LoggerFactory.getLogger(TypePersonnelResource.class);
        
    @Inject
    private TypePersonnelRepository typePersonnelRepository;
    
    @Inject
    private TypePersonnelSearchRepository typePersonnelSearchRepository;
    
    /**
     * POST  /type-personnels : Create a new typePersonnel.
     *
     * @param typePersonnel the typePersonnel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typePersonnel, or with status 400 (Bad Request) if the typePersonnel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-personnels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypePersonnel> createTypePersonnel(@Valid @RequestBody TypePersonnel typePersonnel) throws URISyntaxException {
        log.debug("REST request to save TypePersonnel : {}", typePersonnel);
        if (typePersonnel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typePersonnel", "idexists", "A new typePersonnel cannot already have an ID")).body(null);
        }
        TypePersonnel result = typePersonnelRepository.save(typePersonnel);
        typePersonnelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-personnels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typePersonnel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-personnels : Updates an existing typePersonnel.
     *
     * @param typePersonnel the typePersonnel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typePersonnel,
     * or with status 400 (Bad Request) if the typePersonnel is not valid,
     * or with status 500 (Internal Server Error) if the typePersonnel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-personnels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypePersonnel> updateTypePersonnel(@Valid @RequestBody TypePersonnel typePersonnel) throws URISyntaxException {
        log.debug("REST request to update TypePersonnel : {}", typePersonnel);
        if (typePersonnel.getId() == null) {
            return createTypePersonnel(typePersonnel);
        }
        TypePersonnel result = typePersonnelRepository.save(typePersonnel);
        typePersonnelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typePersonnel", typePersonnel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-personnels : get all the typePersonnels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typePersonnels in body
     */
    @RequestMapping(value = "/type-personnels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypePersonnel> getAllTypePersonnels() {
        log.debug("REST request to get all TypePersonnels");
        List<TypePersonnel> typePersonnels = typePersonnelRepository.findAll();
        return typePersonnels;
    }

    /**
     * GET  /type-personnels/:id : get the "id" typePersonnel.
     *
     * @param id the id of the typePersonnel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typePersonnel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-personnels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypePersonnel> getTypePersonnel(@PathVariable Long id) {
        log.debug("REST request to get TypePersonnel : {}", id);
        TypePersonnel typePersonnel = typePersonnelRepository.findOne(id);
        return Optional.ofNullable(typePersonnel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-personnels/:id : delete the "id" typePersonnel.
     *
     * @param id the id of the typePersonnel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-personnels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypePersonnel(@PathVariable Long id) {
        log.debug("REST request to delete TypePersonnel : {}", id);
        typePersonnelRepository.delete(id);
        typePersonnelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typePersonnel", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-personnels?query=:query : search for the typePersonnel corresponding
     * to the query.
     *
     * @param query the query of the typePersonnel search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-personnels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypePersonnel> searchTypePersonnels(@RequestParam String query) {
        log.debug("REST request to search TypePersonnels for query {}", query);
        return StreamSupport
            .stream(typePersonnelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
