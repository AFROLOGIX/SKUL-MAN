package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Classe;
import com.afrologix.skulman.repository.ClasseRepository;
import com.afrologix.skulman.repository.search.ClasseSearchRepository;
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
 * REST controller for managing Classe.
 */
@RestController
@RequestMapping("/api")
public class ClasseResource {

    private final Logger log = LoggerFactory.getLogger(ClasseResource.class);
        
    @Inject
    private ClasseRepository classeRepository;
    
    @Inject
    private ClasseSearchRepository classeSearchRepository;
    
    /**
     * POST  /classes : Create a new classe.
     *
     * @param classe the classe to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classe, or with status 400 (Bad Request) if the classe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/classes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Classe> createClasse(@Valid @RequestBody Classe classe) throws URISyntaxException {
        log.debug("REST request to save Classe : {}", classe);
        if (classe.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("classe", "idexists", "A new classe cannot already have an ID")).body(null);
        }
        Classe result = classeRepository.save(classe);
        classeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("classe", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /classes : Updates an existing classe.
     *
     * @param classe the classe to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classe,
     * or with status 400 (Bad Request) if the classe is not valid,
     * or with status 500 (Internal Server Error) if the classe couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/classes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Classe> updateClasse(@Valid @RequestBody Classe classe) throws URISyntaxException {
        log.debug("REST request to update Classe : {}", classe);
        if (classe.getId() == null) {
            return createClasse(classe);
        }
        Classe result = classeRepository.save(classe);
        classeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("classe", classe.getId().toString()))
            .body(result);
    }

    /**
     * GET  /classes : get all the classes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of classes in body
     */
    @RequestMapping(value = "/classes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Classe> getAllClasses() {
        log.debug("REST request to get all Classes");
        List<Classe> classes = classeRepository.findAll();
        return classes;
    }

    /**
     * GET  /classes/:id : get the "id" classe.
     *
     * @param id the id of the classe to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classe, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/classes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Classe> getClasse(@PathVariable Long id) {
        log.debug("REST request to get Classe : {}", id);
        Classe classe = classeRepository.findOne(id);
        return Optional.ofNullable(classe)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /classes/:id : delete the "id" classe.
     *
     * @param id the id of the classe to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/classes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        log.debug("REST request to delete Classe : {}", id);
        classeRepository.delete(id);
        classeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("classe", id.toString())).build();
    }

    /**
     * SEARCH  /_search/classes?query=:query : search for the classe corresponding
     * to the query.
     *
     * @param query the query of the classe search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/classes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Classe> searchClasses(@RequestParam String query) {
        log.debug("REST request to search Classes for query {}", query);
        return StreamSupport
            .stream(classeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
