package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.GroupeUtilisateur;
import com.afrologix.skulman.repository.GroupeUtilisateurRepository;
import com.afrologix.skulman.repository.search.GroupeUtilisateurSearchRepository;
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
 * REST controller for managing GroupeUtilisateur.
 */
@RestController
@RequestMapping("/api")
public class GroupeUtilisateurResource {

    private final Logger log = LoggerFactory.getLogger(GroupeUtilisateurResource.class);
        
    @Inject
    private GroupeUtilisateurRepository groupeUtilisateurRepository;
    
    @Inject
    private GroupeUtilisateurSearchRepository groupeUtilisateurSearchRepository;
    
    /**
     * POST  /groupe-utilisateurs : Create a new groupeUtilisateur.
     *
     * @param groupeUtilisateur the groupeUtilisateur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupeUtilisateur, or with status 400 (Bad Request) if the groupeUtilisateur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/groupe-utilisateurs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupeUtilisateur> createGroupeUtilisateur(@Valid @RequestBody GroupeUtilisateur groupeUtilisateur) throws URISyntaxException {
        log.debug("REST request to save GroupeUtilisateur : {}", groupeUtilisateur);
        if (groupeUtilisateur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("groupeUtilisateur", "idexists", "A new groupeUtilisateur cannot already have an ID")).body(null);
        }
        GroupeUtilisateur result = groupeUtilisateurRepository.save(groupeUtilisateur);
        groupeUtilisateurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/groupe-utilisateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("groupeUtilisateur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groupe-utilisateurs : Updates an existing groupeUtilisateur.
     *
     * @param groupeUtilisateur the groupeUtilisateur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupeUtilisateur,
     * or with status 400 (Bad Request) if the groupeUtilisateur is not valid,
     * or with status 500 (Internal Server Error) if the groupeUtilisateur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/groupe-utilisateurs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupeUtilisateur> updateGroupeUtilisateur(@Valid @RequestBody GroupeUtilisateur groupeUtilisateur) throws URISyntaxException {
        log.debug("REST request to update GroupeUtilisateur : {}", groupeUtilisateur);
        if (groupeUtilisateur.getId() == null) {
            return createGroupeUtilisateur(groupeUtilisateur);
        }
        GroupeUtilisateur result = groupeUtilisateurRepository.save(groupeUtilisateur);
        groupeUtilisateurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("groupeUtilisateur", groupeUtilisateur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groupe-utilisateurs : get all the groupeUtilisateurs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of groupeUtilisateurs in body
     */
    @RequestMapping(value = "/groupe-utilisateurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GroupeUtilisateur> getAllGroupeUtilisateurs() {
        log.debug("REST request to get all GroupeUtilisateurs");
        List<GroupeUtilisateur> groupeUtilisateurs = groupeUtilisateurRepository.findAll();
        return groupeUtilisateurs;
    }

    /**
     * GET  /groupe-utilisateurs/:id : get the "id" groupeUtilisateur.
     *
     * @param id the id of the groupeUtilisateur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupeUtilisateur, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/groupe-utilisateurs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GroupeUtilisateur> getGroupeUtilisateur(@PathVariable Long id) {
        log.debug("REST request to get GroupeUtilisateur : {}", id);
        GroupeUtilisateur groupeUtilisateur = groupeUtilisateurRepository.findOne(id);
        return Optional.ofNullable(groupeUtilisateur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /groupe-utilisateurs/:id : delete the "id" groupeUtilisateur.
     *
     * @param id the id of the groupeUtilisateur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/groupe-utilisateurs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGroupeUtilisateur(@PathVariable Long id) {
        log.debug("REST request to delete GroupeUtilisateur : {}", id);
        groupeUtilisateurRepository.delete(id);
        groupeUtilisateurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("groupeUtilisateur", id.toString())).build();
    }

    /**
     * SEARCH  /_search/groupe-utilisateurs?query=:query : search for the groupeUtilisateur corresponding
     * to the query.
     *
     * @param query the query of the groupeUtilisateur search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/groupe-utilisateurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GroupeUtilisateur> searchGroupeUtilisateurs(@RequestParam String query) {
        log.debug("REST request to search GroupeUtilisateurs for query {}", query);
        return StreamSupport
            .stream(groupeUtilisateurSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
