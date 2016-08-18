package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.PaiementPersonnel;
import com.afrologix.skulman.repository.PaiementPersonnelRepository;
import com.afrologix.skulman.repository.search.PaiementPersonnelSearchRepository;
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
 * REST controller for managing PaiementPersonnel.
 */
@RestController
@RequestMapping("/api")
public class PaiementPersonnelResource {

    private final Logger log = LoggerFactory.getLogger(PaiementPersonnelResource.class);
        
    @Inject
    private PaiementPersonnelRepository paiementPersonnelRepository;
    
    @Inject
    private PaiementPersonnelSearchRepository paiementPersonnelSearchRepository;
    
    /**
     * POST  /paiement-personnels : Create a new paiementPersonnel.
     *
     * @param paiementPersonnel the paiementPersonnel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paiementPersonnel, or with status 400 (Bad Request) if the paiementPersonnel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/paiement-personnels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaiementPersonnel> createPaiementPersonnel(@Valid @RequestBody PaiementPersonnel paiementPersonnel) throws URISyntaxException {
        log.debug("REST request to save PaiementPersonnel : {}", paiementPersonnel);
        if (paiementPersonnel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("paiementPersonnel", "idexists", "A new paiementPersonnel cannot already have an ID")).body(null);
        }
        PaiementPersonnel result = paiementPersonnelRepository.save(paiementPersonnel);
        paiementPersonnelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/paiement-personnels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("paiementPersonnel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /paiement-personnels : Updates an existing paiementPersonnel.
     *
     * @param paiementPersonnel the paiementPersonnel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paiementPersonnel,
     * or with status 400 (Bad Request) if the paiementPersonnel is not valid,
     * or with status 500 (Internal Server Error) if the paiementPersonnel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/paiement-personnels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaiementPersonnel> updatePaiementPersonnel(@Valid @RequestBody PaiementPersonnel paiementPersonnel) throws URISyntaxException {
        log.debug("REST request to update PaiementPersonnel : {}", paiementPersonnel);
        if (paiementPersonnel.getId() == null) {
            return createPaiementPersonnel(paiementPersonnel);
        }
        PaiementPersonnel result = paiementPersonnelRepository.save(paiementPersonnel);
        paiementPersonnelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("paiementPersonnel", paiementPersonnel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /paiement-personnels : get all the paiementPersonnels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of paiementPersonnels in body
     */
    @RequestMapping(value = "/paiement-personnels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PaiementPersonnel> getAllPaiementPersonnels() {
        log.debug("REST request to get all PaiementPersonnels");
        List<PaiementPersonnel> paiementPersonnels = paiementPersonnelRepository.findAll();
        return paiementPersonnels;
    }

    /**
     * GET  /paiement-personnels/:id : get the "id" paiementPersonnel.
     *
     * @param id the id of the paiementPersonnel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paiementPersonnel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/paiement-personnels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaiementPersonnel> getPaiementPersonnel(@PathVariable Long id) {
        log.debug("REST request to get PaiementPersonnel : {}", id);
        PaiementPersonnel paiementPersonnel = paiementPersonnelRepository.findOne(id);
        return Optional.ofNullable(paiementPersonnel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /paiement-personnels/:id : delete the "id" paiementPersonnel.
     *
     * @param id the id of the paiementPersonnel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/paiement-personnels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePaiementPersonnel(@PathVariable Long id) {
        log.debug("REST request to delete PaiementPersonnel : {}", id);
        paiementPersonnelRepository.delete(id);
        paiementPersonnelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("paiementPersonnel", id.toString())).build();
    }

    /**
     * SEARCH  /_search/paiement-personnels?query=:query : search for the paiementPersonnel corresponding
     * to the query.
     *
     * @param query the query of the paiementPersonnel search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/paiement-personnels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PaiementPersonnel> searchPaiementPersonnels(@RequestParam String query) {
        log.debug("REST request to search PaiementPersonnels for query {}", query);
        return StreamSupport
            .stream(paiementPersonnelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
