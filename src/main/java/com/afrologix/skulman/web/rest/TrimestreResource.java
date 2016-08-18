package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Trimestre;
import com.afrologix.skulman.repository.TrimestreRepository;
import com.afrologix.skulman.repository.search.TrimestreSearchRepository;
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
 * REST controller for managing Trimestre.
 */
@RestController
@RequestMapping("/api")
public class TrimestreResource {

    private final Logger log = LoggerFactory.getLogger(TrimestreResource.class);
        
    @Inject
    private TrimestreRepository trimestreRepository;
    
    @Inject
    private TrimestreSearchRepository trimestreSearchRepository;
    
    /**
     * POST  /trimestres : Create a new trimestre.
     *
     * @param trimestre the trimestre to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trimestre, or with status 400 (Bad Request) if the trimestre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/trimestres",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trimestre> createTrimestre(@Valid @RequestBody Trimestre trimestre) throws URISyntaxException {
        log.debug("REST request to save Trimestre : {}", trimestre);
        if (trimestre.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("trimestre", "idexists", "A new trimestre cannot already have an ID")).body(null);
        }
        Trimestre result = trimestreRepository.save(trimestre);
        trimestreSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/trimestres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("trimestre", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trimestres : Updates an existing trimestre.
     *
     * @param trimestre the trimestre to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trimestre,
     * or with status 400 (Bad Request) if the trimestre is not valid,
     * or with status 500 (Internal Server Error) if the trimestre couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/trimestres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trimestre> updateTrimestre(@Valid @RequestBody Trimestre trimestre) throws URISyntaxException {
        log.debug("REST request to update Trimestre : {}", trimestre);
        if (trimestre.getId() == null) {
            return createTrimestre(trimestre);
        }
        Trimestre result = trimestreRepository.save(trimestre);
        trimestreSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("trimestre", trimestre.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trimestres : get all the trimestres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of trimestres in body
     */
    @RequestMapping(value = "/trimestres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Trimestre> getAllTrimestres() {
        log.debug("REST request to get all Trimestres");
        List<Trimestre> trimestres = trimestreRepository.findAll();
        return trimestres;
    }

    /**
     * GET  /trimestres/:id : get the "id" trimestre.
     *
     * @param id the id of the trimestre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trimestre, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/trimestres/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trimestre> getTrimestre(@PathVariable Long id) {
        log.debug("REST request to get Trimestre : {}", id);
        Trimestre trimestre = trimestreRepository.findOne(id);
        return Optional.ofNullable(trimestre)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trimestres/:id : delete the "id" trimestre.
     *
     * @param id the id of the trimestre to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/trimestres/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTrimestre(@PathVariable Long id) {
        log.debug("REST request to delete Trimestre : {}", id);
        trimestreRepository.delete(id);
        trimestreSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trimestre", id.toString())).build();
    }

    /**
     * SEARCH  /_search/trimestres?query=:query : search for the trimestre corresponding
     * to the query.
     *
     * @param query the query of the trimestre search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/trimestres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Trimestre> searchTrimestres(@RequestParam String query) {
        log.debug("REST request to search Trimestres for query {}", query);
        return StreamSupport
            .stream(trimestreSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
