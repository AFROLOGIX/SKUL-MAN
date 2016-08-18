package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.FormatMatricule;
import com.afrologix.skulman.repository.FormatMatriculeRepository;
import com.afrologix.skulman.repository.search.FormatMatriculeSearchRepository;
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
 * REST controller for managing FormatMatricule.
 */
@RestController
@RequestMapping("/api")
public class FormatMatriculeResource {

    private final Logger log = LoggerFactory.getLogger(FormatMatriculeResource.class);
        
    @Inject
    private FormatMatriculeRepository formatMatriculeRepository;
    
    @Inject
    private FormatMatriculeSearchRepository formatMatriculeSearchRepository;
    
    /**
     * POST  /format-matricules : Create a new formatMatricule.
     *
     * @param formatMatricule the formatMatricule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formatMatricule, or with status 400 (Bad Request) if the formatMatricule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/format-matricules",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FormatMatricule> createFormatMatricule(@Valid @RequestBody FormatMatricule formatMatricule) throws URISyntaxException {
        log.debug("REST request to save FormatMatricule : {}", formatMatricule);
        if (formatMatricule.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("formatMatricule", "idexists", "A new formatMatricule cannot already have an ID")).body(null);
        }
        FormatMatricule result = formatMatriculeRepository.save(formatMatricule);
        formatMatriculeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/format-matricules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("formatMatricule", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /format-matricules : Updates an existing formatMatricule.
     *
     * @param formatMatricule the formatMatricule to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formatMatricule,
     * or with status 400 (Bad Request) if the formatMatricule is not valid,
     * or with status 500 (Internal Server Error) if the formatMatricule couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/format-matricules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FormatMatricule> updateFormatMatricule(@Valid @RequestBody FormatMatricule formatMatricule) throws URISyntaxException {
        log.debug("REST request to update FormatMatricule : {}", formatMatricule);
        if (formatMatricule.getId() == null) {
            return createFormatMatricule(formatMatricule);
        }
        FormatMatricule result = formatMatriculeRepository.save(formatMatricule);
        formatMatriculeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("formatMatricule", formatMatricule.getId().toString()))
            .body(result);
    }

    /**
     * GET  /format-matricules : get all the formatMatricules.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of formatMatricules in body
     */
    @RequestMapping(value = "/format-matricules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FormatMatricule> getAllFormatMatricules() {
        log.debug("REST request to get all FormatMatricules");
        List<FormatMatricule> formatMatricules = formatMatriculeRepository.findAll();
        return formatMatricules;
    }

    /**
     * GET  /format-matricules/:id : get the "id" formatMatricule.
     *
     * @param id the id of the formatMatricule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formatMatricule, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/format-matricules/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FormatMatricule> getFormatMatricule(@PathVariable Long id) {
        log.debug("REST request to get FormatMatricule : {}", id);
        FormatMatricule formatMatricule = formatMatriculeRepository.findOne(id);
        return Optional.ofNullable(formatMatricule)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /format-matricules/:id : delete the "id" formatMatricule.
     *
     * @param id the id of the formatMatricule to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/format-matricules/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFormatMatricule(@PathVariable Long id) {
        log.debug("REST request to delete FormatMatricule : {}", id);
        formatMatriculeRepository.delete(id);
        formatMatriculeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("formatMatricule", id.toString())).build();
    }

    /**
     * SEARCH  /_search/format-matricules?query=:query : search for the formatMatricule corresponding
     * to the query.
     *
     * @param query the query of the formatMatricule search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/format-matricules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FormatMatricule> searchFormatMatricules(@RequestParam String query) {
        log.debug("REST request to search FormatMatricules for query {}", query);
        return StreamSupport
            .stream(formatMatriculeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
