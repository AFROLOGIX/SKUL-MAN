package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.TypeChambre;
import com.afrologix.skulman.repository.TypeChambreRepository;
import com.afrologix.skulman.repository.search.TypeChambreSearchRepository;
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
 * REST controller for managing TypeChambre.
 */
@RestController
@RequestMapping("/api")
public class TypeChambreResource {

    private final Logger log = LoggerFactory.getLogger(TypeChambreResource.class);
        
    @Inject
    private TypeChambreRepository typeChambreRepository;
    
    @Inject
    private TypeChambreSearchRepository typeChambreSearchRepository;
    
    /**
     * POST  /type-chambres : Create a new typeChambre.
     *
     * @param typeChambre the typeChambre to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeChambre, or with status 400 (Bad Request) if the typeChambre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-chambres",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeChambre> createTypeChambre(@Valid @RequestBody TypeChambre typeChambre) throws URISyntaxException {
        log.debug("REST request to save TypeChambre : {}", typeChambre);
        if (typeChambre.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeChambre", "idexists", "A new typeChambre cannot already have an ID")).body(null);
        }
        TypeChambre result = typeChambreRepository.save(typeChambre);
        typeChambreSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-chambres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeChambre", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-chambres : Updates an existing typeChambre.
     *
     * @param typeChambre the typeChambre to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeChambre,
     * or with status 400 (Bad Request) if the typeChambre is not valid,
     * or with status 500 (Internal Server Error) if the typeChambre couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-chambres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeChambre> updateTypeChambre(@Valid @RequestBody TypeChambre typeChambre) throws URISyntaxException {
        log.debug("REST request to update TypeChambre : {}", typeChambre);
        if (typeChambre.getId() == null) {
            return createTypeChambre(typeChambre);
        }
        TypeChambre result = typeChambreRepository.save(typeChambre);
        typeChambreSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeChambre", typeChambre.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-chambres : get all the typeChambres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeChambres in body
     */
    @RequestMapping(value = "/type-chambres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeChambre> getAllTypeChambres() {
        log.debug("REST request to get all TypeChambres");
        List<TypeChambre> typeChambres = typeChambreRepository.findAll();
        return typeChambres;
    }

    /**
     * GET  /type-chambres/:id : get the "id" typeChambre.
     *
     * @param id the id of the typeChambre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeChambre, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-chambres/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeChambre> getTypeChambre(@PathVariable Long id) {
        log.debug("REST request to get TypeChambre : {}", id);
        TypeChambre typeChambre = typeChambreRepository.findOne(id);
        return Optional.ofNullable(typeChambre)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-chambres/:id : delete the "id" typeChambre.
     *
     * @param id the id of the typeChambre to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-chambres/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypeChambre(@PathVariable Long id) {
        log.debug("REST request to delete TypeChambre : {}", id);
        typeChambreRepository.delete(id);
        typeChambreSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeChambre", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-chambres?query=:query : search for the typeChambre corresponding
     * to the query.
     *
     * @param query the query of the typeChambre search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-chambres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeChambre> searchTypeChambres(@RequestParam String query) {
        log.debug("REST request to search TypeChambres for query {}", query);
        return StreamSupport
            .stream(typeChambreSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
