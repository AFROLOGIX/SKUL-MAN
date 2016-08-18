package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.ChambreEleve;
import com.afrologix.skulman.repository.ChambreEleveRepository;
import com.afrologix.skulman.repository.search.ChambreEleveSearchRepository;

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
 * Test class for the ChambreEleveResource REST controller.
 *
 * @see ChambreEleveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class ChambreEleveResourceIntTest {

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBB";

    @Inject
    private ChambreEleveRepository chambreEleveRepository;

    @Inject
    private ChambreEleveSearchRepository chambreEleveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restChambreEleveMockMvc;

    private ChambreEleve chambreEleve;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChambreEleveResource chambreEleveResource = new ChambreEleveResource();
        ReflectionTestUtils.setField(chambreEleveResource, "chambreEleveSearchRepository", chambreEleveSearchRepository);
        ReflectionTestUtils.setField(chambreEleveResource, "chambreEleveRepository", chambreEleveRepository);
        this.restChambreEleveMockMvc = MockMvcBuilders.standaloneSetup(chambreEleveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        chambreEleveSearchRepository.deleteAll();
        chambreEleve = new ChambreEleve();
        chambreEleve.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createChambreEleve() throws Exception {
        int databaseSizeBeforeCreate = chambreEleveRepository.findAll().size();

        // Create the ChambreEleve

        restChambreEleveMockMvc.perform(post("/api/chambre-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chambreEleve)))
                .andExpect(status().isCreated());

        // Validate the ChambreEleve in the database
        List<ChambreEleve> chambreEleves = chambreEleveRepository.findAll();
        assertThat(chambreEleves).hasSize(databaseSizeBeforeCreate + 1);
        ChambreEleve testChambreEleve = chambreEleves.get(chambreEleves.size() - 1);
        assertThat(testChambreEleve.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the ChambreEleve in ElasticSearch
        ChambreEleve chambreEleveEs = chambreEleveSearchRepository.findOne(testChambreEleve.getId());
        assertThat(chambreEleveEs).isEqualToComparingFieldByField(testChambreEleve);
    }

    @Test
    @Transactional
    public void getAllChambreEleves() throws Exception {
        // Initialize the database
        chambreEleveRepository.saveAndFlush(chambreEleve);

        // Get all the chambreEleves
        restChambreEleveMockMvc.perform(get("/api/chambre-eleves?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(chambreEleve.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getChambreEleve() throws Exception {
        // Initialize the database
        chambreEleveRepository.saveAndFlush(chambreEleve);

        // Get the chambreEleve
        restChambreEleveMockMvc.perform(get("/api/chambre-eleves/{id}", chambreEleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(chambreEleve.getId().intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChambreEleve() throws Exception {
        // Get the chambreEleve
        restChambreEleveMockMvc.perform(get("/api/chambre-eleves/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChambreEleve() throws Exception {
        // Initialize the database
        chambreEleveRepository.saveAndFlush(chambreEleve);
        chambreEleveSearchRepository.save(chambreEleve);
        int databaseSizeBeforeUpdate = chambreEleveRepository.findAll().size();

        // Update the chambreEleve
        ChambreEleve updatedChambreEleve = new ChambreEleve();
        updatedChambreEleve.setId(chambreEleve.getId());
        updatedChambreEleve.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restChambreEleveMockMvc.perform(put("/api/chambre-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChambreEleve)))
                .andExpect(status().isOk());

        // Validate the ChambreEleve in the database
        List<ChambreEleve> chambreEleves = chambreEleveRepository.findAll();
        assertThat(chambreEleves).hasSize(databaseSizeBeforeUpdate);
        ChambreEleve testChambreEleve = chambreEleves.get(chambreEleves.size() - 1);
        assertThat(testChambreEleve.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the ChambreEleve in ElasticSearch
        ChambreEleve chambreEleveEs = chambreEleveSearchRepository.findOne(testChambreEleve.getId());
        assertThat(chambreEleveEs).isEqualToComparingFieldByField(testChambreEleve);
    }

    @Test
    @Transactional
    public void deleteChambreEleve() throws Exception {
        // Initialize the database
        chambreEleveRepository.saveAndFlush(chambreEleve);
        chambreEleveSearchRepository.save(chambreEleve);
        int databaseSizeBeforeDelete = chambreEleveRepository.findAll().size();

        // Get the chambreEleve
        restChambreEleveMockMvc.perform(delete("/api/chambre-eleves/{id}", chambreEleve.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean chambreEleveExistsInEs = chambreEleveSearchRepository.exists(chambreEleve.getId());
        assertThat(chambreEleveExistsInEs).isFalse();

        // Validate the database is empty
        List<ChambreEleve> chambreEleves = chambreEleveRepository.findAll();
        assertThat(chambreEleves).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChambreEleve() throws Exception {
        // Initialize the database
        chambreEleveRepository.saveAndFlush(chambreEleve);
        chambreEleveSearchRepository.save(chambreEleve);

        // Search the chambreEleve
        restChambreEleveMockMvc.perform(get("/api/_search/chambre-eleves?query=id:" + chambreEleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chambreEleve.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
