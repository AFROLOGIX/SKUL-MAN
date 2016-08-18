package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Chambre;
import com.afrologix.skulman.repository.ChambreRepository;
import com.afrologix.skulman.repository.search.ChambreSearchRepository;
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
 * REST controller for managing Chambre.
 */
@RestController
@RequestMapping("/api")
public class ChambreResource {

    private final Logger log = LoggerFactory.getLogger(ChambreResource.class);
        
    @Inject
    private ChambreRepository chambreRepository;
    
    @Inject
    private ChambreSearchRepository chambreSearchRepository;
    
    /**
     * POST  /chambres : Create a new chambre.
     *
     * @param chambre the chambre to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chambre, or with status 400 (Bad Request) if the chambre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/chambres",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chambre> createChambre(@Valid @RequestBody Chambre chambre) throws URISyntaxException {
        log.debug("REST request to save Chambre : {}", chambre);
        if (chambre.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("chambre", "idexists", "A new chambre cannot already have an ID")).body(null);
        }
        Chambre result = chambreRepository.save(chambre);
        chambreSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/chambres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("chambre", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chambres : Updates an existing chambre.
     *
     * @param chambre the chambre to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chambre,
     * or with status 400 (Bad Request) if the chambre is not valid,
     * or with status 500 (Internal Server Error) if the chambre couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/chambres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chambre> updateChambre(@Valid @RequestBody Chambre chambre) throws URISyntaxException {
        log.debug("REST request to update Chambre : {}", chambre);
        if (chambre.getId() == null) {
            return createChambre(chambre);
        }
        Chambre result = chambreRepository.save(chambre);
        chambreSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("chambre", chambre.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chambres : get all the chambres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of chambres in body
     */
    @RequestMapping(value = "/chambres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Chambre> getAllChambres() {
        log.debug("REST request to get all Chambres");
        List<Chambre> chambres = chambreRepository.findAll();
        return chambres;
    }

    /**
     * GET  /chambres/:id : get the "id" chambre.
     *
     * @param id the id of the chambre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chambre, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/chambres/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chambre> getChambre(@PathVariable Long id) {
        log.debug("REST request to get Chambre : {}", id);
        Chambre chambre = chambreRepository.findOne(id);
        return Optional.ofNullable(chambre)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /chambres/:id : delete the "id" chambre.
     *
     * @param id the id of the chambre to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/chambres/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChambre(@PathVariable Long id) {
        log.debug("REST request to delete Chambre : {}", id);
        chambreRepository.delete(id);
        chambreSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("chambre", id.toString())).build();
    }

    /**
     * SEARCH  /_search/chambres?query=:query : search for the chambre corresponding
     * to the query.
     *
     * @param query the query of the chambre search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/chambres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Chambre> searchChambres(@RequestParam String query) {
        log.debug("REST request to search Chambres for query {}", query);
        return StreamSupport
            .stream(chambreSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
