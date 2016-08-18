package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Epreuve;
import com.afrologix.skulman.repository.EpreuveRepository;
import com.afrologix.skulman.repository.search.EpreuveSearchRepository;

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
 * Test class for the EpreuveResource REST controller.
 *
 * @see EpreuveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class EpreuveResourceIntTest {

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private EpreuveRepository epreuveRepository;

    @Inject
    private EpreuveSearchRepository epreuveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEpreuveMockMvc;

    private Epreuve epreuve;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EpreuveResource epreuveResource = new EpreuveResource();
        ReflectionTestUtils.setField(epreuveResource, "epreuveSearchRepository", epreuveSearchRepository);
        ReflectionTestUtils.setField(epreuveResource, "epreuveRepository", epreuveRepository);
        this.restEpreuveMockMvc = MockMvcBuilders.standaloneSetup(epreuveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        epreuveSearchRepository.deleteAll();
        epreuve = new Epreuve();
        epreuve.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createEpreuve() throws Exception {
        int databaseSizeBeforeCreate = epreuveRepository.findAll().size();

        // Create the Epreuve

        restEpreuveMockMvc.perform(post("/api/epreuves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(epreuve)))
                .andExpect(status().isCreated());

        // Validate the Epreuve in the database
        List<Epreuve> epreuves = epreuveRepository.findAll();
        assertThat(epreuves).hasSize(databaseSizeBeforeCreate + 1);
        Epreuve testEpreuve = epreuves.get(epreuves.size() - 1);
        assertThat(testEpreuve.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the Epreuve in ElasticSearch
        Epreuve epreuveEs = epreuveSearchRepository.findOne(testEpreuve.getId());
        assertThat(epreuveEs).isEqualToComparingFieldByField(testEpreuve);
    }

    @Test
    @Transactional
    public void getAllEpreuves() throws Exception {
        // Initialize the database
        epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuves
        restEpreuveMockMvc.perform(get("/api/epreuves?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(epreuve.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getEpreuve() throws Exception {
        // Initialize the database
        epreuveRepository.saveAndFlush(epreuve);

        // Get the epreuve
        restEpreuveMockMvc.perform(get("/api/epreuves/{id}", epreuve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(epreuve.getId().intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEpreuve() throws Exception {
        // Get the epreuve
        restEpreuveMockMvc.perform(get("/api/epreuves/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEpreuve() throws Exception {
        // Initialize the database
        epreuveRepository.saveAndFlush(epreuve);
        epreuveSearchRepository.save(epreuve);
        int databaseSizeBeforeUpdate = epreuveRepository.findAll().size();

        // Update the epreuve
        Epreuve updatedEpreuve = new Epreuve();
        updatedEpreuve.setId(epreuve.getId());
        updatedEpreuve.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restEpreuveMockMvc.perform(put("/api/epreuves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEpreuve)))
                .andExpect(status().isOk());

        // Validate the Epreuve in the database
        List<Epreuve> epreuves = epreuveRepository.findAll();
        assertThat(epreuves).hasSize(databaseSizeBeforeUpdate);
        Epreuve testEpreuve = epreuves.get(epreuves.size() - 1);
        assertThat(testEpreuve.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the Epreuve in ElasticSearch
        Epreuve epreuveEs = epreuveSearchRepository.findOne(testEpreuve.getId());
        assertThat(epreuveEs).isEqualToComparingFieldByField(testEpreuve);
    }

    @Test
    @Transactional
    public void deleteEpreuve() throws Exception {
        // Initialize the database
        epreuveRepository.saveAndFlush(epreuve);
        epreuveSearchRepository.save(epreuve);
        int databaseSizeBeforeDelete = epreuveRepository.findAll().size();

        // Get the epreuve
        restEpreuveMockMvc.perform(delete("/api/epreuves/{id}", epreuve.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean epreuveExistsInEs = epreuveSearchRepository.exists(epreuve.getId());
        assertThat(epreuveExistsInEs).isFalse();

        // Validate the database is empty
        List<Epreuve> epreuves = epreuveRepository.findAll();
        assertThat(epreuves).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEpreuve() throws Exception {
        // Initialize the database
        epreuveRepository.saveAndFlush(epreuve);
        epreuveSearchRepository.save(epreuve);

        // Search the epreuve
        restEpreuveMockMvc.perform(get("/api/_search/epreuves?query=id:" + epreuve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epreuve.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
