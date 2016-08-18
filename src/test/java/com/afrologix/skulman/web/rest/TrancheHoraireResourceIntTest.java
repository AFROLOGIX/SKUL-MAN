package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.TrancheHoraire;
import com.afrologix.skulman.repository.TrancheHoraireRepository;
import com.afrologix.skulman.repository.search.TrancheHoraireSearchRepository;

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
 * Test class for the TrancheHoraireResource REST controller.
 *
 * @see TrancheHoraireResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TrancheHoraireResourceIntTest {


    @Inject
    private TrancheHoraireRepository trancheHoraireRepository;

    @Inject
    private TrancheHoraireSearchRepository trancheHoraireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTrancheHoraireMockMvc;

    private TrancheHoraire trancheHoraire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrancheHoraireResource trancheHoraireResource = new TrancheHoraireResource();
        ReflectionTestUtils.setField(trancheHoraireResource, "trancheHoraireSearchRepository", trancheHoraireSearchRepository);
        ReflectionTestUtils.setField(trancheHoraireResource, "trancheHoraireRepository", trancheHoraireRepository);
        this.restTrancheHoraireMockMvc = MockMvcBuilders.standaloneSetup(trancheHoraireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        trancheHoraireSearchRepository.deleteAll();
        trancheHoraire = new TrancheHoraire();
    }

    @Test
    @Transactional
    public void createTrancheHoraire() throws Exception {
        int databaseSizeBeforeCreate = trancheHoraireRepository.findAll().size();

        // Create the TrancheHoraire

        restTrancheHoraireMockMvc.perform(post("/api/tranche-horaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trancheHoraire)))
                .andExpect(status().isCreated());

        // Validate the TrancheHoraire in the database
        List<TrancheHoraire> trancheHoraires = trancheHoraireRepository.findAll();
        assertThat(trancheHoraires).hasSize(databaseSizeBeforeCreate + 1);
        TrancheHoraire testTrancheHoraire = trancheHoraires.get(trancheHoraires.size() - 1);

        // Validate the TrancheHoraire in ElasticSearch
        TrancheHoraire trancheHoraireEs = trancheHoraireSearchRepository.findOne(testTrancheHoraire.getId());
        assertThat(trancheHoraireEs).isEqualToComparingFieldByField(testTrancheHoraire);
    }

    @Test
    @Transactional
    public void getAllTrancheHoraires() throws Exception {
        // Initialize the database
        trancheHoraireRepository.saveAndFlush(trancheHoraire);

        // Get all the trancheHoraires
        restTrancheHoraireMockMvc.perform(get("/api/tranche-horaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trancheHoraire.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTrancheHoraire() throws Exception {
        // Initialize the database
        trancheHoraireRepository.saveAndFlush(trancheHoraire);

        // Get the trancheHoraire
        restTrancheHoraireMockMvc.perform(get("/api/tranche-horaires/{id}", trancheHoraire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(trancheHoraire.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTrancheHoraire() throws Exception {
        // Get the trancheHoraire
        restTrancheHoraireMockMvc.perform(get("/api/tranche-horaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrancheHoraire() throws Exception {
        // Initialize the database
        trancheHoraireRepository.saveAndFlush(trancheHoraire);
        trancheHoraireSearchRepository.save(trancheHoraire);
        int databaseSizeBeforeUpdate = trancheHoraireRepository.findAll().size();

        // Update the trancheHoraire
        TrancheHoraire updatedTrancheHoraire = new TrancheHoraire();
        updatedTrancheHoraire.setId(trancheHoraire.getId());

        restTrancheHoraireMockMvc.perform(put("/api/tranche-horaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTrancheHoraire)))
                .andExpect(status().isOk());

        // Validate the TrancheHoraire in the database
        List<TrancheHoraire> trancheHoraires = trancheHoraireRepository.findAll();
        assertThat(trancheHoraires).hasSize(databaseSizeBeforeUpdate);
        TrancheHoraire testTrancheHoraire = trancheHoraires.get(trancheHoraires.size() - 1);

        // Validate the TrancheHoraire in ElasticSearch
        TrancheHoraire trancheHoraireEs = trancheHoraireSearchRepository.findOne(testTrancheHoraire.getId());
        assertThat(trancheHoraireEs).isEqualToComparingFieldByField(testTrancheHoraire);
    }

    @Test
    @Transactional
    public void deleteTrancheHoraire() throws Exception {
        // Initialize the database
        trancheHoraireRepository.saveAndFlush(trancheHoraire);
        trancheHoraireSearchRepository.save(trancheHoraire);
        int databaseSizeBeforeDelete = trancheHoraireRepository.findAll().size();

        // Get the trancheHoraire
        restTrancheHoraireMockMvc.perform(delete("/api/tranche-horaires/{id}", trancheHoraire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean trancheHoraireExistsInEs = trancheHoraireSearchRepository.exists(trancheHoraire.getId());
        assertThat(trancheHoraireExistsInEs).isFalse();

        // Validate the database is empty
        List<TrancheHoraire> trancheHoraires = trancheHoraireRepository.findAll();
        assertThat(trancheHoraires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTrancheHoraire() throws Exception {
        // Initialize the database
        trancheHoraireRepository.saveAndFlush(trancheHoraire);
        trancheHoraireSearchRepository.save(trancheHoraire);

        // Search the trancheHoraire
        restTrancheHoraireMockMvc.perform(get("/api/_search/tranche-horaires?query=id:" + trancheHoraire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trancheHoraire.getId().intValue())));
    }
}
