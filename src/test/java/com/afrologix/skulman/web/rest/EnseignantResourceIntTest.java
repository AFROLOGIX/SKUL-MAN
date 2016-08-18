package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Enseignant;
import com.afrologix.skulman.repository.EnseignantRepository;
import com.afrologix.skulman.repository.search.EnseignantSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EnseignantResource REST controller.
 *
 * @see EnseignantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class EnseignantResourceIntTest {

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_SPECIALITE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_SPECIALITE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private EnseignantRepository enseignantRepository;

    @Inject
    private EnseignantSearchRepository enseignantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEnseignantMockMvc;

    private Enseignant enseignant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnseignantResource enseignantResource = new EnseignantResource();
        ReflectionTestUtils.setField(enseignantResource, "enseignantSearchRepository", enseignantSearchRepository);
        ReflectionTestUtils.setField(enseignantResource, "enseignantRepository", enseignantRepository);
        this.restEnseignantMockMvc = MockMvcBuilders.standaloneSetup(enseignantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        enseignantSearchRepository.deleteAll();
        enseignant = new Enseignant();
        enseignant.setMatricule(DEFAULT_MATRICULE);
        enseignant.setSpecialite(DEFAULT_SPECIALITE);
    }

    @Test
    @Transactional
    public void createEnseignant() throws Exception {
        int databaseSizeBeforeCreate = enseignantRepository.findAll().size();

        // Create the Enseignant

        restEnseignantMockMvc.perform(post("/api/enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enseignant)))
                .andExpect(status().isCreated());

        // Validate the Enseignant in the database
        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeCreate + 1);
        Enseignant testEnseignant = enseignants.get(enseignants.size() - 1);
        assertThat(testEnseignant.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testEnseignant.getSpecialite()).isEqualTo(DEFAULT_SPECIALITE);

        // Validate the Enseignant in ElasticSearch
        Enseignant enseignantEs = enseignantSearchRepository.findOne(testEnseignant.getId());
        assertThat(enseignantEs).isEqualToComparingFieldByField(testEnseignant);
    }

    @Test
    @Transactional
    public void getAllEnseignants() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignants
        restEnseignantMockMvc.perform(get("/api/enseignants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
                .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.toString())))
                .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE.toString())));
    }

    @Test
    @Transactional
    public void getEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get the enseignant
        restEnseignantMockMvc.perform(get("/api/enseignants/{id}", enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(enseignant.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE.toString()))
            .andExpect(jsonPath("$.specialite").value(DEFAULT_SPECIALITE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnseignant() throws Exception {
        // Get the enseignant
        restEnseignantMockMvc.perform(get("/api/enseignants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);
        enseignantSearchRepository.save(enseignant);
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();

        // Update the enseignant
        Enseignant updatedEnseignant = new Enseignant();
        updatedEnseignant.setId(enseignant.getId());
        updatedEnseignant.setMatricule(UPDATED_MATRICULE);
        updatedEnseignant.setSpecialite(UPDATED_SPECIALITE);

        restEnseignantMockMvc.perform(put("/api/enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEnseignant)))
                .andExpect(status().isOk());

        // Validate the Enseignant in the database
        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeUpdate);
        Enseignant testEnseignant = enseignants.get(enseignants.size() - 1);
        assertThat(testEnseignant.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testEnseignant.getSpecialite()).isEqualTo(UPDATED_SPECIALITE);

        // Validate the Enseignant in ElasticSearch
        Enseignant enseignantEs = enseignantSearchRepository.findOne(testEnseignant.getId());
        assertThat(enseignantEs).isEqualToComparingFieldByField(testEnseignant);
    }

    @Test
    @Transactional
    public void deleteEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);
        enseignantSearchRepository.save(enseignant);
        int databaseSizeBeforeDelete = enseignantRepository.findAll().size();

        // Get the enseignant
        restEnseignantMockMvc.perform(delete("/api/enseignants/{id}", enseignant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean enseignantExistsInEs = enseignantSearchRepository.exists(enseignant.getId());
        assertThat(enseignantExistsInEs).isFalse();

        // Validate the database is empty
        List<Enseignant> enseignants = enseignantRepository.findAll();
        assertThat(enseignants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);
        enseignantSearchRepository.save(enseignant);

        // Search the enseignant
        restEnseignantMockMvc.perform(get("/api/_search/enseignants?query=id:" + enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.toString())))
            .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE.toString())));
    }
}
