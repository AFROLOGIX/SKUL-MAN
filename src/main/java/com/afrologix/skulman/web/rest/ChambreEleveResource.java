package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.ChambreEleve;
import com.afrologix.skulman.repository.ChambreEleveRepository;
import com.afrologix.skulman.repository.search.ChambreEleveSearchRepository;
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
 * REST controller for managing ChambreEleve.
 */
@RestController
@RequestMapping("/api")
public class ChambreEleveResource {

    private final Logger log = LoggerFactory.getLogger(ChambreEleveResource.class);
        
    @Inject
    private ChambreEleveRepository chambreEleveRepository;
    
    @Inject
    private ChambreEleveSearchRepository chambreEleveSearchRepository;
    
    /**
     * POST  /chambre-eleves : Create a new chambreEleve.
     *
     * @param chambreEleve the chambreEleve to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chambreEleve, or with status 400 (Bad Request) if the chambreEleve has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/chambre-eleves",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChambreEleve> createChambreEleve(@Valid @RequestBody ChambreEleve chambreEleve) throws URISyntaxException {
        log.debug("REST request to save ChambreEleve : {}", chambreEleve);
        if (chambreEleve.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("chambreEleve", "idexists", "A new chambreEleve cannot already have an ID")).body(null);
        }
        ChambreEleve result = chambreEleveRepository.save(chambreEleve);
        chambreEleveSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/chambre-eleves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("chambreEleve", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chambre-eleves : Updates an existing chambreEleve.
     *
     * @param chambreEleve the chambreEleve to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chambreEleve,
     * or with status 400 (Bad Request) if the chambreEleve is not valid,
     * or with status 500 (Internal Server Error) if the chambreEleve couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/chambre-eleves",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChambreEleve> updateChambreEleve(@Valid @RequestBody ChambreEleve chambreEleve) throws URISyntaxException {
        log.debug("REST request to update ChambreEleve : {}", chambreEleve);
        if (chambreEleve.getId() == null) {
            return createChambreEleve(chambreEleve);
        }
        ChambreEleve result = chambreEleveRepository.save(chambreEleve);
        chambreEleveSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("chambreEleve", chambreEleve.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chambre-eleves : get all the chambreEleves.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of chambreEleves in body
     */
    @RequestMapping(value = "/chambre-eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ChambreEleve> getAllChambreEleves() {
        log.debug("REST request to get all ChambreEleves");
        List<ChambreEleve> chambreEleves = chambreEleveRepository.findAll();
        return chambreEleves;
    }

    /**
     * GET  /chambre-eleves/:id : get the "id" chambreEleve.
     *
     * @param id the id of the chambreEleve to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chambreEleve, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/chambre-eleves/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChambreEleve> getChambreEleve(@PathVariable Long id) {
        log.debug("REST request to get ChambreEleve : {}", id);
        ChambreEleve chambreEleve = chambreEleveRepository.findOne(id);
        return Optional.ofNullable(chambreEleve)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /chambre-eleves/:id : delete the "id" chambreEleve.
     *
     * @param id the id of the chambreEleve to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/chambre-eleves/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChambreEleve(@PathVariable Long id) {
        log.debug("REST request to delete ChambreEleve : {}", id);
        chambreEleveRepository.delete(id);
        chambreEleveSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("chambreEleve", id.toString())).build();
    }

    /**
     * SEARCH  /_search/chambre-eleves?query=:query : search for the chambreEleve corresponding
     * to the query.
     *
     * @param query the query of the chambreEleve search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/chambre-eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ChambreEleve> searchChambreEleves(@RequestParam String query) {
        log.debug("REST request to search ChambreEleves for query {}", query);
        return StreamSupport
            .stream(chambreEleveSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
