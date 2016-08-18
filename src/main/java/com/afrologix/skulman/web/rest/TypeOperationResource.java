package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.TypeOperation;
import com.afrologix.skulman.repository.TypeOperationRepository;
import com.afrologix.skulman.repository.search.TypeOperationSearchRepository;
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
 * REST controller for managing TypeOperation.
 */
@RestController
@RequestMapping("/api")
public class TypeOperationResource {

    private final Logger log = LoggerFactory.getLogger(TypeOperationResource.class);
        
    @Inject
    private TypeOperationRepository typeOperationRepository;
    
    @Inject
    private TypeOperationSearchRepository typeOperationSearchRepository;
    
    /**
     * POST  /type-operations : Create a new typeOperation.
     *
     * @param typeOperation the typeOperation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeOperation, or with status 400 (Bad Request) if the typeOperation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-operations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeOperation> createTypeOperation(@Valid @RequestBody TypeOperation typeOperation) throws URISyntaxException {
        log.debug("REST request to save TypeOperation : {}", typeOperation);
        if (typeOperation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeOperation", "idexists", "A new typeOperation cannot already have an ID")).body(null);
        }
        TypeOperation result = typeOperationRepository.save(typeOperation);
        typeOperationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-operations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeOperation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-operations : Updates an existing typeOperation.
     *
     * @param typeOperation the typeOperation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeOperation,
     * or with status 400 (Bad Request) if the typeOperation is not valid,
     * or with status 500 (Internal Server Error) if the typeOperation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-operations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeOperation> updateTypeOperation(@Valid @RequestBody TypeOperation typeOperation) throws URISyntaxException {
        log.debug("REST request to update TypeOperation : {}", typeOperation);
        if (typeOperation.getId() == null) {
            return createTypeOperation(typeOperation);
        }
        TypeOperation result = typeOperationRepository.save(typeOperation);
        typeOperationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeOperation", typeOperation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-operations : get all the typeOperations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeOperations in body
     */
    @RequestMapping(value = "/type-operations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeOperation> getAllTypeOperations() {
        log.debug("REST request to get all TypeOperations");
        List<TypeOperation> typeOperations = typeOperationRepository.findAll();
        return typeOperations;
    }

    /**
     * GET  /type-operations/:id : get the "id" typeOperation.
     *
     * @param id the id of the typeOperation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeOperation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-operations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeOperation> getTypeOperation(@PathVariable Long id) {
        log.debug("REST request to get TypeOperation : {}", id);
        TypeOperation typeOperation = typeOperationRepository.findOne(id);
        return Optional.ofNullable(typeOperation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-operations/:id : delete the "id" typeOperation.
     *
     * @param id the id of the typeOperation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-operations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypeOperation(@PathVariable Long id) {
        log.debug("REST request to delete TypeOperation : {}", id);
        typeOperationRepository.delete(id);
        typeOperationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeOperation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-operations?query=:query : search for the typeOperation corresponding
     * to the query.
     *
     * @param query the query of the typeOperation search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-operations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeOperation> searchTypeOperations(@RequestParam String query) {
        log.debug("REST request to search TypeOperations for query {}", query);
        return StreamSupport
            .stream(typeOperationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
