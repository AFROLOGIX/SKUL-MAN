package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Section;
import com.afrologix.skulman.repository.SectionRepository;
import com.afrologix.skulman.repository.search.SectionSearchRepository;
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
 * REST controller for managing Section.
 */
@RestController
@RequestMapping("/api")
public class SectionResource {

    private final Logger log = LoggerFactory.getLogger(SectionResource.class);
        
    @Inject
    private SectionRepository sectionRepository;
    
    @Inject
    private SectionSearchRepository sectionSearchRepository;
    
    /**
     * POST  /sections : Create a new section.
     *
     * @param section the section to create
     * @return the ResponseEntity with status 201 (Created) and with body the new section, or with status 400 (Bad Request) if the section has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sections",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Section> createSection(@Valid @RequestBody Section section) throws URISyntaxException {
        log.debug("REST request to save Section : {}", section);
        if (section.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("section", "idexists", "A new section cannot already have an ID")).body(null);
        }
        Section result = sectionRepository.save(section);
        sectionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("section", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sections : Updates an existing section.
     *
     * @param section the section to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated section,
     * or with status 400 (Bad Request) if the section is not valid,
     * or with status 500 (Internal Server Error) if the section couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sections",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Section> updateSection(@Valid @RequestBody Section section) throws URISyntaxException {
        log.debug("REST request to update Section : {}", section);
        if (section.getId() == null) {
            return createSection(section);
        }
        Section result = sectionRepository.save(section);
        sectionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("section", section.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sections : get all the sections.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sections in body
     */
    @RequestMapping(value = "/sections",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Section> getAllSections() {
        log.debug("REST request to get all Sections");
        List<Section> sections = sectionRepository.findAll();
        return sections;
    }

    /**
     * GET  /sections/:id : get the "id" section.
     *
     * @param id the id of the section to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the section, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sections/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Section> getSection(@PathVariable Long id) {
        log.debug("REST request to get Section : {}", id);
        Section section = sectionRepository.findOne(id);
        return Optional.ofNullable(section)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sections/:id : delete the "id" section.
     *
     * @param id the id of the section to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sections/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        log.debug("REST request to delete Section : {}", id);
        sectionRepository.delete(id);
        sectionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("section", id.toString())).build();
    }

    /**
     * SEARCH  /_search/sections?query=:query : search for the section corresponding
     * to the query.
     *
     * @param query the query of the section search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/sections",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Section> searchSections(@RequestParam String query) {
        log.debug("REST request to search Sections for query {}", query);
        return StreamSupport
            .stream(sectionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
