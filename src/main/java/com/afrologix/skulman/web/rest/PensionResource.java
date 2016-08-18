package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Pension;
import com.afrologix.skulman.repository.PensionRepository;
import com.afrologix.skulman.repository.search.PensionSearchRepository;
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
 * REST controller for managing Pension.
 */
@RestController
@RequestMapping("/api")
public class PensionResource {

    private final Logger log = LoggerFactory.getLogger(PensionResource.class);
        
    @Inject
    private PensionRepository pensionRepository;
    
    @Inject
    private PensionSearchRepository pensionSearchRepository;
    
    /**
     * POST  /pensions : Create a new pension.
     *
     * @param pension the pension to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pension, or with status 400 (Bad Request) if the pension has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pensions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pension> createPension(@Valid @RequestBody Pension pension) throws URISyntaxException {
        log.debug("REST request to save Pension : {}", pension);
        if (pension.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pension", "idexists", "A new pension cannot already have an ID")).body(null);
        }
        Pension result = pensionRepository.save(pension);
        pensionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pension", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pensions : Updates an existing pension.
     *
     * @param pension the pension to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pension,
     * or with status 400 (Bad Request) if the pension is not valid,
     * or with status 500 (Internal Server Error) if the pension couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pensions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pension> updatePension(@Valid @RequestBody Pension pension) throws URISyntaxException {
        log.debug("REST request to update Pension : {}", pension);
        if (pension.getId() == null) {
            return createPension(pension);
        }
        Pension result = pensionRepository.save(pension);
        pensionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pension", pension.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pensions : get all the pensions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pensions in body
     */
    @RequestMapping(value = "/pensions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pension> getAllPensions() {
        log.debug("REST request to get all Pensions");
        List<Pension> pensions = pensionRepository.findAll();
        return pensions;
    }

    /**
     * GET  /pensions/:id : get the "id" pension.
     *
     * @param id the id of the pension to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pension, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pensions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pension> getPension(@PathVariable Long id) {
        log.debug("REST request to get Pension : {}", id);
        Pension pension = pensionRepository.findOne(id);
        return Optional.ofNullable(pension)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pensions/:id : delete the "id" pension.
     *
     * @param id the id of the pension to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pensions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePension(@PathVariable Long id) {
        log.debug("REST request to delete Pension : {}", id);
        pensionRepository.delete(id);
        pensionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pension", id.toString())).build();
    }

    /**
     * SEARCH  /_search/pensions?query=:query : search for the pension corresponding
     * to the query.
     *
     * @param query the query of the pension search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/pensions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pension> searchPensions(@RequestParam String query) {
        log.debug("REST request to search Pensions for query {}", query);
        return StreamSupport
            .stream(pensionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
