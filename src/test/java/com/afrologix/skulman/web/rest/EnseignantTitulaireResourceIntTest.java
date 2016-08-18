package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.EnseignantTitulaire;
import com.afrologix.skulman.repository.EnseignantTitulaireRepository;
import com.afrologix.skulman.repository.search.EnseignantTitulaireSearchRepository;

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
 * Test class for the EnseignantTitulaireResource REST controller.
 *
 * @see EnseignantTitulaireResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class EnseignantTitulaireResourceIntTest {

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private EnseignantTitulaireRepository enseignantTitulaireRepository;

    @Inject
    private EnseignantTitulaireSearchRepository enseignantTitulaireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEnseignantTitulaireMockMvc;

    private EnseignantTitulaire enseignantTitulaire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnseignantTitulaireResource enseignantTitulaireResource = new EnseignantTitulaireResource();
        ReflectionTestUtils.setField(enseignantTitulaireResource, "enseignantTitulaireSearchRepository", enseignantTitulaireSearchRepository);
        ReflectionTestUtils.setField(enseignantTitulaireResource, "enseignantTitulaireRepository", enseignantTitulaireRepository);
        this.restEnseignantTitulaireMockMvc = MockMvcBuilders.standaloneSetup(enseignantTitulaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        enseignantTitulaireSearchRepository.deleteAll();
        enseignantTitulaire = new EnseignantTitulaire();
        enseignantTitulaire.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createEnseignantTitulaire() throws Exception {
        int databaseSizeBeforeCreate = enseignantTitulaireRepository.findAll().size();

        // Create the EnseignantTitulaire

        restEnseignantTitulaireMockMvc.perform(post("/api/enseignant-titulaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enseignantTitulaire)))
                .andExpect(status().isCreated());

        // Validate the EnseignantTitulaire in the database
        List<EnseignantTitulaire> enseignantTitulaires = enseignantTitulaireRepository.findAll();
        assertThat(enseignantTitulaires).hasSize(databaseSizeBeforeCreate + 1);
        EnseignantTitulaire testEnseignantTitulaire = enseignantTitulaires.get(enseignantTitulaires.size() - 1);
        assertThat(testEnseignantTitulaire.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the EnseignantTitulaire in ElasticSearch
        EnseignantTitulaire enseignantTitulaireEs = enseignantTitulaireSearchRepository.findOne(testEnseignantTitulaire.getId());
        assertThat(enseignantTitulaireEs).isEqualToComparingFieldByField(testEnseignantTitulaire);
    }

    @Test
    @Transactional
    public void getAllEnseignantTitulaires() throws Exception {
        // Initialize the database
        enseignantTitulaireRepository.saveAndFlush(enseignantTitulaire);

        // Get all the enseignantTitulaires
        restEnseignantTitulaireMockMvc.perform(get("/api/enseignant-titulaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(enseignantTitulaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getEnseignantTitulaire() throws Exception {
        // Initialize the database
        enseignantTitulaireRepository.saveAndFlush(enseignantTitulaire);

        // Get the enseignantTitulaire
        restEnseignantTitulaireMockMvc.perform(get("/api/enseignant-titulaires/{id}", enseignantTitulaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(enseignantTitulaire.getId().intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnseignantTitulaire() throws Exception {
        // Get the enseignantTitulaire
        restEnseignantTitulaireMockMvc.perform(get("/api/enseignant-titulaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnseignantTitulaire() throws Exception {
        // Initialize the database
        enseignantTitulaireRepository.saveAndFlush(enseignantTitulaire);
        enseignantTitulaireSearchRepository.save(enseignantTitulaire);
        int databaseSizeBeforeUpdate = enseignantTitulaireRepository.findAll().size();

        // Update the enseignantTitulaire
        EnseignantTitulaire updatedEnseignantTitulaire = new EnseignantTitulaire();
        updatedEnseignantTitulaire.setId(enseignantTitulaire.getId());
        updatedEnseignantTitulaire.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restEnseignantTitulaireMockMvc.perform(put("/api/enseignant-titulaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEnseignantTitulaire)))
                .andExpect(status().isOk());

        // Validate the EnseignantTitulaire in the database
        List<EnseignantTitulaire> enseignantTitulaires = enseignantTitulaireRepository.findAll();
        assertThat(enseignantTitulaires).hasSize(databaseSizeBeforeUpdate);
        EnseignantTitulaire testEnseignantTitulaire = enseignantTitulaires.get(enseignantTitulaires.size() - 1);
        assertThat(testEnseignantTitulaire.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the EnseignantTitulaire in ElasticSearch
        EnseignantTitulaire enseignantTitulaireEs = enseignantTitulaireSearchRepository.findOne(testEnseignantTitulaire.getId());
        assertThat(enseignantTitulaireEs).isEqualToComparingFieldByField(testEnseignantTitulaire);
    }

    @Test
    @Transactional
    public void deleteEnseignantTitulaire() throws Exception {
        // Initialize the database
        enseignantTitulaireRepository.saveAndFlush(enseignantTitulaire);
        enseignantTitulaireSearchRepository.save(enseignantTitulaire);
        int databaseSizeBeforeDelete = enseignantTitulaireRepository.findAll().size();

        // Get the enseignantTitulaire
        restEnseignantTitulaireMockMvc.perform(delete("/api/enseignant-titulaires/{id}", enseignantTitulaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean enseignantTitulaireExistsInEs = enseignantTitulaireSearchRepository.exists(enseignantTitulaire.getId());
        assertThat(enseignantTitulaireExistsInEs).isFalse();

        // Validate the database is empty
        List<EnseignantTitulaire> enseignantTitulaires = enseignantTitulaireRepository.findAll();
        assertThat(enseignantTitulaires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEnseignantTitulaire() throws Exception {
        // Initialize the database
        enseignantTitulaireRepository.saveAndFlush(enseignantTitulaire);
        enseignantTitulaireSearchRepository.save(enseignantTitulaire);

        // Search the enseignantTitulaire
        restEnseignantTitulaireMockMvc.perform(get("/api/_search/enseignant-titulaires?query=id:" + enseignantTitulaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignantTitulaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
