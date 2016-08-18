package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.RegimePension;
import com.afrologix.skulman.repository.RegimePensionRepository;
import com.afrologix.skulman.repository.search.RegimePensionSearchRepository;
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
 * REST controller for managing RegimePension.
 */
@RestController
@RequestMapping("/api")
public class RegimePensionResource {

    private final Logger log = LoggerFactory.getLogger(RegimePensionResource.class);
        
    @Inject
    private RegimePensionRepository regimePensionRepository;
    
    @Inject
    private RegimePensionSearchRepository regimePensionSearchRepository;
    
    /**
     * POST  /regime-pensions : Create a new regimePension.
     *
     * @param regimePension the regimePension to create
     * @return the ResponseEntity with status 201 (Created) and with body the new regimePension, or with status 400 (Bad Request) if the regimePension has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/regime-pensions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RegimePension> createRegimePension(@Valid @RequestBody RegimePension regimePension) throws URISyntaxException {
        log.debug("REST request to save RegimePension : {}", regimePension);
        if (regimePension.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("regimePension", "idexists", "A new regimePension cannot already have an ID")).body(null);
        }
        RegimePension result = regimePensionRepository.save(regimePension);
        regimePensionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/regime-pensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("regimePension", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /regime-pensions : Updates an existing regimePension.
     *
     * @param regimePension the regimePension to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated regimePension,
     * or with status 400 (Bad Request) if the regimePension is not valid,
     * or with status 500 (Internal Server Error) if the regimePension couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/regime-pensions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RegimePension> updateRegimePension(@Valid @RequestBody RegimePension regimePension) throws URISyntaxException {
        log.debug("REST request to update RegimePension : {}", regimePension);
        if (regimePension.getId() == null) {
            return createRegimePension(regimePension);
        }
        RegimePension result = regimePensionRepository.save(regimePension);
        regimePensionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("regimePension", regimePension.getId().toString()))
            .body(result);
    }

    /**
     * GET  /regime-pensions : get all the regimePensions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of regimePensions in body
     */
    @RequestMapping(value = "/regime-pensions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RegimePension> getAllRegimePensions() {
        log.debug("REST request to get all RegimePensions");
        List<RegimePension> regimePensions = regimePensionRepository.findAll();
        return regimePensions;
    }

    /**
     * GET  /regime-pensions/:id : get the "id" regimePension.
     *
     * @param id the id of the regimePension to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the regimePension, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/regime-pensions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RegimePension> getRegimePension(@PathVariable Long id) {
        log.debug("REST request to get RegimePension : {}", id);
        RegimePension regimePension = regimePensionRepository.findOne(id);
        return Optional.ofNullable(regimePension)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /regime-pensions/:id : delete the "id" regimePension.
     *
     * @param id the id of the regimePension to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/regime-pensions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRegimePension(@PathVariable Long id) {
        log.debug("REST request to delete RegimePension : {}", id);
        regimePensionRepository.delete(id);
        regimePensionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("regimePension", id.toString())).build();
    }

    /**
     * SEARCH  /_search/regime-pensions?query=:query : search for the regimePension corresponding
     * to the query.
     *
     * @param query the query of the regimePension search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/regime-pensions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RegimePension> searchRegimePensions(@RequestParam String query) {
        log.debug("REST request to search RegimePensions for query {}", query);
        return StreamSupport
            .stream(regimePensionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
