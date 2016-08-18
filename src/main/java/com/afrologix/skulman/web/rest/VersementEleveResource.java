package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.VersementEleve;
import com.afrologix.skulman.repository.VersementEleveRepository;
import com.afrologix.skulman.repository.search.VersementEleveSearchRepository;
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
 * REST controller for managing VersementEleve.
 */
@RestController
@RequestMapping("/api")
public class VersementEleveResource {

    private final Logger log = LoggerFactory.getLogger(VersementEleveResource.class);
        
    @Inject
    private VersementEleveRepository versementEleveRepository;
    
    @Inject
    private VersementEleveSearchRepository versementEleveSearchRepository;
    
    /**
     * POST  /versement-eleves : Create a new versementEleve.
     *
     * @param versementEleve the versementEleve to create
     * @return the ResponseEntity with status 201 (Created) and with body the new versementEleve, or with status 400 (Bad Request) if the versementEleve has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/versement-eleves",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VersementEleve> createVersementEleve(@Valid @RequestBody VersementEleve versementEleve) throws URISyntaxException {
        log.debug("REST request to save VersementEleve : {}", versementEleve);
        if (versementEleve.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("versementEleve", "idexists", "A new versementEleve cannot already have an ID")).body(null);
        }
        VersementEleve result = versementEleveRepository.save(versementEleve);
        versementEleveSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/versement-eleves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("versementEleve", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /versement-eleves : Updates an existing versementEleve.
     *
     * @param versementEleve the versementEleve to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated versementEleve,
     * or with status 400 (Bad Request) if the versementEleve is not valid,
     * or with status 500 (Internal Server Error) if the versementEleve couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/versement-eleves",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VersementEleve> updateVersementEleve(@Valid @RequestBody VersementEleve versementEleve) throws URISyntaxException {
        log.debug("REST request to update VersementEleve : {}", versementEleve);
        if (versementEleve.getId() == null) {
            return createVersementEleve(versementEleve);
        }
        VersementEleve result = versementEleveRepository.save(versementEleve);
        versementEleveSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("versementEleve", versementEleve.getId().toString()))
            .body(result);
    }

    /**
     * GET  /versement-eleves : get all the versementEleves.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of versementEleves in body
     */
    @RequestMapping(value = "/versement-eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<VersementEleve> getAllVersementEleves() {
        log.debug("REST request to get all VersementEleves");
        List<VersementEleve> versementEleves = versementEleveRepository.findAll();
        return versementEleves;
    }

    /**
     * GET  /versement-eleves/:id : get the "id" versementEleve.
     *
     * @param id the id of the versementEleve to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the versementEleve, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/versement-eleves/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VersementEleve> getVersementEleve(@PathVariable Long id) {
        log.debug("REST request to get VersementEleve : {}", id);
        VersementEleve versementEleve = versementEleveRepository.findOne(id);
        return Optional.ofNullable(versementEleve)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /versement-eleves/:id : delete the "id" versementEleve.
     *
     * @param id the id of the versementEleve to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/versement-eleves/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVersementEleve(@PathVariable Long id) {
        log.debug("REST request to delete VersementEleve : {}", id);
        versementEleveRepository.delete(id);
        versementEleveSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("versementEleve", id.toString())).build();
    }

    /**
     * SEARCH  /_search/versement-eleves?query=:query : search for the versementEleve corresponding
     * to the query.
     *
     * @param query the query of the versementEleve search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/versement-eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<VersementEleve> searchVersementEleves(@RequestParam String query) {
        log.debug("REST request to search VersementEleves for query {}", query);
        return StreamSupport
            .stream(versementEleveSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
