package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.TypeAbonnementBus;
import com.afrologix.skulman.repository.TypeAbonnementBusRepository;
import com.afrologix.skulman.repository.search.TypeAbonnementBusSearchRepository;
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
 * REST controller for managing TypeAbonnementBus.
 */
@RestController
@RequestMapping("/api")
public class TypeAbonnementBusResource {

    private final Logger log = LoggerFactory.getLogger(TypeAbonnementBusResource.class);
        
    @Inject
    private TypeAbonnementBusRepository typeAbonnementBusRepository;
    
    @Inject
    private TypeAbonnementBusSearchRepository typeAbonnementBusSearchRepository;
    
    /**
     * POST  /type-abonnement-buses : Create a new typeAbonnementBus.
     *
     * @param typeAbonnementBus the typeAbonnementBus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeAbonnementBus, or with status 400 (Bad Request) if the typeAbonnementBus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-abonnement-buses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeAbonnementBus> createTypeAbonnementBus(@Valid @RequestBody TypeAbonnementBus typeAbonnementBus) throws URISyntaxException {
        log.debug("REST request to save TypeAbonnementBus : {}", typeAbonnementBus);
        if (typeAbonnementBus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeAbonnementBus", "idexists", "A new typeAbonnementBus cannot already have an ID")).body(null);
        }
        TypeAbonnementBus result = typeAbonnementBusRepository.save(typeAbonnementBus);
        typeAbonnementBusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-abonnement-buses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeAbonnementBus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-abonnement-buses : Updates an existing typeAbonnementBus.
     *
     * @param typeAbonnementBus the typeAbonnementBus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeAbonnementBus,
     * or with status 400 (Bad Request) if the typeAbonnementBus is not valid,
     * or with status 500 (Internal Server Error) if the typeAbonnementBus couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-abonnement-buses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeAbonnementBus> updateTypeAbonnementBus(@Valid @RequestBody TypeAbonnementBus typeAbonnementBus) throws URISyntaxException {
        log.debug("REST request to update TypeAbonnementBus : {}", typeAbonnementBus);
        if (typeAbonnementBus.getId() == null) {
            return createTypeAbonnementBus(typeAbonnementBus);
        }
        TypeAbonnementBus result = typeAbonnementBusRepository.save(typeAbonnementBus);
        typeAbonnementBusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeAbonnementBus", typeAbonnementBus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-abonnement-buses : get all the typeAbonnementBuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeAbonnementBuses in body
     */
    @RequestMapping(value = "/type-abonnement-buses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeAbonnementBus> getAllTypeAbonnementBuses() {
        log.debug("REST request to get all TypeAbonnementBuses");
        List<TypeAbonnementBus> typeAbonnementBuses = typeAbonnementBusRepository.findAll();
        return typeAbonnementBuses;
    }

    /**
     * GET  /type-abonnement-buses/:id : get the "id" typeAbonnementBus.
     *
     * @param id the id of the typeAbonnementBus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeAbonnementBus, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-abonnement-buses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeAbonnementBus> getTypeAbonnementBus(@PathVariable Long id) {
        log.debug("REST request to get TypeAbonnementBus : {}", id);
        TypeAbonnementBus typeAbonnementBus = typeAbonnementBusRepository.findOne(id);
        return Optional.ofNullable(typeAbonnementBus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-abonnement-buses/:id : delete the "id" typeAbonnementBus.
     *
     * @param id the id of the typeAbonnementBus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-abonnement-buses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypeAbonnementBus(@PathVariable Long id) {
        log.debug("REST request to delete TypeAbonnementBus : {}", id);
        typeAbonnementBusRepository.delete(id);
        typeAbonnementBusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeAbonnementBus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-abonnement-buses?query=:query : search for the typeAbonnementBus corresponding
     * to the query.
     *
     * @param query the query of the typeAbonnementBus search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-abonnement-buses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeAbonnementBus> searchTypeAbonnementBuses(@RequestParam String query) {
        log.debug("REST request to search TypeAbonnementBuses for query {}", query);
        return StreamSupport
            .stream(typeAbonnementBusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
