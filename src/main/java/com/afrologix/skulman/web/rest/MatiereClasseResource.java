package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.MatiereClasse;
import com.afrologix.skulman.repository.MatiereClasseRepository;
import com.afrologix.skulman.repository.search.MatiereClasseSearchRepository;
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
 * REST controller for managing MatiereClasse.
 */
@RestController
@RequestMapping("/api")
public class MatiereClasseResource {

    private final Logger log = LoggerFactory.getLogger(MatiereClasseResource.class);
        
    @Inject
    private MatiereClasseRepository matiereClasseRepository;
    
    @Inject
    private MatiereClasseSearchRepository matiereClasseSearchRepository;
    
    /**
     * POST  /matiere-classes : Create a new matiereClasse.
     *
     * @param matiereClasse the matiereClasse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new matiereClasse, or with status 400 (Bad Request) if the matiereClasse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/matiere-classes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MatiereClasse> createMatiereClasse(@Valid @RequestBody MatiereClasse matiereClasse) throws URISyntaxException {
        log.debug("REST request to save MatiereClasse : {}", matiereClasse);
        if (matiereClasse.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("matiereClasse", "idexists", "A new matiereClasse cannot already have an ID")).body(null);
        }
        MatiereClasse result = matiereClasseRepository.save(matiereClasse);
        matiereClasseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/matiere-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("matiereClasse", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /matiere-classes : Updates an existing matiereClasse.
     *
     * @param matiereClasse the matiereClasse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated matiereClasse,
     * or with status 400 (Bad Request) if the matiereClasse is not valid,
     * or with status 500 (Internal Server Error) if the matiereClasse couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/matiere-classes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MatiereClasse> updateMatiereClasse(@Valid @RequestBody MatiereClasse matiereClasse) throws URISyntaxException {
        log.debug("REST request to update MatiereClasse : {}", matiereClasse);
        if (matiereClasse.getId() == null) {
            return createMatiereClasse(matiereClasse);
        }
        MatiereClasse result = matiereClasseRepository.save(matiereClasse);
        matiereClasseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("matiereClasse", matiereClasse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /matiere-classes : get all the matiereClasses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of matiereClasses in body
     */
    @RequestMapping(value = "/matiere-classes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MatiereClasse> getAllMatiereClasses() {
        log.debug("REST request to get all MatiereClasses");
        List<MatiereClasse> matiereClasses = matiereClasseRepository.findAll();
        return matiereClasses;
    }

    /**
     * GET  /matiere-classes/:id : get the "id" matiereClasse.
     *
     * @param id the id of the matiereClasse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the matiereClasse, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/matiere-classes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MatiereClasse> getMatiereClasse(@PathVariable Long id) {
        log.debug("REST request to get MatiereClasse : {}", id);
        MatiereClasse matiereClasse = matiereClasseRepository.findOne(id);
        return Optional.ofNullable(matiereClasse)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /matiere-classes/:id : delete the "id" matiereClasse.
     *
     * @param id the id of the matiereClasse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/matiere-classes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMatiereClasse(@PathVariable Long id) {
        log.debug("REST request to delete MatiereClasse : {}", id);
        matiereClasseRepository.delete(id);
        matiereClasseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("matiereClasse", id.toString())).build();
    }

    /**
     * SEARCH  /_search/matiere-classes?query=:query : search for the matiereClasse corresponding
     * to the query.
     *
     * @param query the query of the matiereClasse search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/matiere-classes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MatiereClasse> searchMatiereClasses(@RequestParam String query) {
        log.debug("REST request to search MatiereClasses for query {}", query);
        return StreamSupport
            .stream(matiereClasseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
