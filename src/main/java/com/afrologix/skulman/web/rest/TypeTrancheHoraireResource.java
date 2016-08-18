package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.TypeTrancheHoraire;
import com.afrologix.skulman.repository.TypeTrancheHoraireRepository;
import com.afrologix.skulman.repository.search.TypeTrancheHoraireSearchRepository;
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
 * REST controller for managing TypeTrancheHoraire.
 */
@RestController
@RequestMapping("/api")
public class TypeTrancheHoraireResource {

    private final Logger log = LoggerFactory.getLogger(TypeTrancheHoraireResource.class);
        
    @Inject
    private TypeTrancheHoraireRepository typeTrancheHoraireRepository;
    
    @Inject
    private TypeTrancheHoraireSearchRepository typeTrancheHoraireSearchRepository;
    
    /**
     * POST  /type-tranche-horaires : Create a new typeTrancheHoraire.
     *
     * @param typeTrancheHoraire the typeTrancheHoraire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeTrancheHoraire, or with status 400 (Bad Request) if the typeTrancheHoraire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-tranche-horaires",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeTrancheHoraire> createTypeTrancheHoraire(@Valid @RequestBody TypeTrancheHoraire typeTrancheHoraire) throws URISyntaxException {
        log.debug("REST request to save TypeTrancheHoraire : {}", typeTrancheHoraire);
        if (typeTrancheHoraire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeTrancheHoraire", "idexists", "A new typeTrancheHoraire cannot already have an ID")).body(null);
        }
        TypeTrancheHoraire result = typeTrancheHoraireRepository.save(typeTrancheHoraire);
        typeTrancheHoraireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-tranche-horaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeTrancheHoraire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-tranche-horaires : Updates an existing typeTrancheHoraire.
     *
     * @param typeTrancheHoraire the typeTrancheHoraire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeTrancheHoraire,
     * or with status 400 (Bad Request) if the typeTrancheHoraire is not valid,
     * or with status 500 (Internal Server Error) if the typeTrancheHoraire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-tranche-horaires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeTrancheHoraire> updateTypeTrancheHoraire(@Valid @RequestBody TypeTrancheHoraire typeTrancheHoraire) throws URISyntaxException {
        log.debug("REST request to update TypeTrancheHoraire : {}", typeTrancheHoraire);
        if (typeTrancheHoraire.getId() == null) {
            return createTypeTrancheHoraire(typeTrancheHoraire);
        }
        TypeTrancheHoraire result = typeTrancheHoraireRepository.save(typeTrancheHoraire);
        typeTrancheHoraireSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeTrancheHoraire", typeTrancheHoraire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-tranche-horaires : get all the typeTrancheHoraires.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeTrancheHoraires in body
     */
    @RequestMapping(value = "/type-tranche-horaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeTrancheHoraire> getAllTypeTrancheHoraires() {
        log.debug("REST request to get all TypeTrancheHoraires");
        List<TypeTrancheHoraire> typeTrancheHoraires = typeTrancheHoraireRepository.findAll();
        return typeTrancheHoraires;
    }

    /**
     * GET  /type-tranche-horaires/:id : get the "id" typeTrancheHoraire.
     *
     * @param id the id of the typeTrancheHoraire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeTrancheHoraire, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-tranche-horaires/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeTrancheHoraire> getTypeTrancheHoraire(@PathVariable Long id) {
        log.debug("REST request to get TypeTrancheHoraire : {}", id);
        TypeTrancheHoraire typeTrancheHoraire = typeTrancheHoraireRepository.findOne(id);
        return Optional.ofNullable(typeTrancheHoraire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-tranche-horaires/:id : delete the "id" typeTrancheHoraire.
     *
     * @param id the id of the typeTrancheHoraire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-tranche-horaires/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypeTrancheHoraire(@PathVariable Long id) {
        log.debug("REST request to delete TypeTrancheHoraire : {}", id);
        typeTrancheHoraireRepository.delete(id);
        typeTrancheHoraireSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeTrancheHoraire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-tranche-horaires?query=:query : search for the typeTrancheHoraire corresponding
     * to the query.
     *
     * @param query the query of the typeTrancheHoraire search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-tranche-horaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeTrancheHoraire> searchTypeTrancheHoraires(@RequestParam String query) {
        log.debug("REST request to search TypeTrancheHoraires for query {}", query);
        return StreamSupport
            .stream(typeTrancheHoraireSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
