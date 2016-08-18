package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.ClasseEleve;
import com.afrologix.skulman.repository.ClasseEleveRepository;
import com.afrologix.skulman.repository.search.ClasseEleveSearchRepository;
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
 * REST controller for managing ClasseEleve.
 */
@RestController
@RequestMapping("/api")
public class ClasseEleveResource {

    private final Logger log = LoggerFactory.getLogger(ClasseEleveResource.class);
        
    @Inject
    private ClasseEleveRepository classeEleveRepository;
    
    @Inject
    private ClasseEleveSearchRepository classeEleveSearchRepository;
    
    /**
     * POST  /classe-eleves : Create a new classeEleve.
     *
     * @param classeEleve the classeEleve to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classeEleve, or with status 400 (Bad Request) if the classeEleve has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/classe-eleves",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClasseEleve> createClasseEleve(@Valid @RequestBody ClasseEleve classeEleve) throws URISyntaxException {
        log.debug("REST request to save ClasseEleve : {}", classeEleve);
        if (classeEleve.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("classeEleve", "idexists", "A new classeEleve cannot already have an ID")).body(null);
        }
        ClasseEleve result = classeEleveRepository.save(classeEleve);
        classeEleveSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/classe-eleves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("classeEleve", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /classe-eleves : Updates an existing classeEleve.
     *
     * @param classeEleve the classeEleve to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classeEleve,
     * or with status 400 (Bad Request) if the classeEleve is not valid,
     * or with status 500 (Internal Server Error) if the classeEleve couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/classe-eleves",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClasseEleve> updateClasseEleve(@Valid @RequestBody ClasseEleve classeEleve) throws URISyntaxException {
        log.debug("REST request to update ClasseEleve : {}", classeEleve);
        if (classeEleve.getId() == null) {
            return createClasseEleve(classeEleve);
        }
        ClasseEleve result = classeEleveRepository.save(classeEleve);
        classeEleveSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("classeEleve", classeEleve.getId().toString()))
            .body(result);
    }

    /**
     * GET  /classe-eleves : get all the classeEleves.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of classeEleves in body
     */
    @RequestMapping(value = "/classe-eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ClasseEleve> getAllClasseEleves() {
        log.debug("REST request to get all ClasseEleves");
        List<ClasseEleve> classeEleves = classeEleveRepository.findAll();
        return classeEleves;
    }

    /**
     * GET  /classe-eleves/:id : get the "id" classeEleve.
     *
     * @param id the id of the classeEleve to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classeEleve, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/classe-eleves/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClasseEleve> getClasseEleve(@PathVariable Long id) {
        log.debug("REST request to get ClasseEleve : {}", id);
        ClasseEleve classeEleve = classeEleveRepository.findOne(id);
        return Optional.ofNullable(classeEleve)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /classe-eleves/:id : delete the "id" classeEleve.
     *
     * @param id the id of the classeEleve to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/classe-eleves/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClasseEleve(@PathVariable Long id) {
        log.debug("REST request to delete ClasseEleve : {}", id);
        classeEleveRepository.delete(id);
        classeEleveSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("classeEleve", id.toString())).build();
    }

    /**
     * SEARCH  /_search/classe-eleves?query=:query : search for the classeEleve corresponding
     * to the query.
     *
     * @param query the query of the classeEleve search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/classe-eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ClasseEleve> searchClasseEleves(@RequestParam String query) {
        log.debug("REST request to search ClasseEleves for query {}", query);
        return StreamSupport
            .stream(classeEleveSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
