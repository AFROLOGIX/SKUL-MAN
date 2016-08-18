package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Batiment;
import com.afrologix.skulman.repository.BatimentRepository;
import com.afrologix.skulman.repository.search.BatimentSearchRepository;

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
 * Test class for the BatimentResource REST controller.
 *
 * @see BatimentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class BatimentResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LOCALISATION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LOCALISATION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private BatimentRepository batimentRepository;

    @Inject
    private BatimentSearchRepository batimentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBatimentMockMvc;

    private Batiment batiment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BatimentResource batimentResource = new BatimentResource();
        ReflectionTestUtils.setField(batimentResource, "batimentSearchRepository", batimentSearchRepository);
        ReflectionTestUtils.setField(batimentResource, "batimentRepository", batimentRepository);
        this.restBatimentMockMvc = MockMvcBuilders.standaloneSetup(batimentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        batimentSearchRepository.deleteAll();
        batiment = new Batiment();
        batiment.setCode(DEFAULT_CODE);
        batiment.setLibelleFr(DEFAULT_LIBELLE_FR);
        batiment.setLibelleEn(DEFAULT_LIBELLE_EN);
        batiment.setLocalisation(DEFAULT_LOCALISATION);
    }

    @Test
    @Transactional
    public void createBatiment() throws Exception {
        int databaseSizeBeforeCreate = batimentRepository.findAll().size();

        // Create the Batiment

        restBatimentMockMvc.perform(post("/api/batiments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(batiment)))
                .andExpect(status().isCreated());

        // Validate the Batiment in the database
        List<Batiment> batiments = batimentRepository.findAll();
        assertThat(batiments).hasSize(databaseSizeBeforeCreate + 1);
        Batiment testBatiment = batiments.get(batiments.size() - 1);
        assertThat(testBatiment.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBatiment.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testBatiment.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testBatiment.getLocalisation()).isEqualTo(DEFAULT_LOCALISATION);

        // Validate the Batiment in ElasticSearch
        Batiment batimentEs = batimentSearchRepository.findOne(testBatiment.getId());
        assertThat(batimentEs).isEqualToComparingFieldByField(testBatiment);
    }

    @Test
    @Transactional
    public void getAllBatiments() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);

        // Get all the batiments
        restBatimentMockMvc.perform(get("/api/batiments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(batiment.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION.toString())));
    }

    @Test
    @Transactional
    public void getBatiment() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);

        // Get the batiment
        restBatimentMockMvc.perform(get("/api/batiments/{id}", batiment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(batiment.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.localisation").value(DEFAULT_LOCALISATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBatiment() throws Exception {
        // Get the batiment
        restBatimentMockMvc.perform(get("/api/batiments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBatiment() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);
        batimentSearchRepository.save(batiment);
        int databaseSizeBeforeUpdate = batimentRepository.findAll().size();

        // Update the batiment
        Batiment updatedBatiment = new Batiment();
        updatedBatiment.setId(batiment.getId());
        updatedBatiment.setCode(UPDATED_CODE);
        updatedBatiment.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedBatiment.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedBatiment.setLocalisation(UPDATED_LOCALISATION);

        restBatimentMockMvc.perform(put("/api/batiments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBatiment)))
                .andExpect(status().isOk());

        // Validate the Batiment in the database
        List<Batiment> batiments = batimentRepository.findAll();
        assertThat(batiments).hasSize(databaseSizeBeforeUpdate);
        Batiment testBatiment = batiments.get(batiments.size() - 1);
        assertThat(testBatiment.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBatiment.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testBatiment.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testBatiment.getLocalisation()).isEqualTo(UPDATED_LOCALISATION);

        // Validate the Batiment in ElasticSearch
        Batiment batimentEs = batimentSearchRepository.findOne(testBatiment.getId());
        assertThat(batimentEs).isEqualToComparingFieldByField(testBatiment);
    }

    @Test
    @Transactional
    public void deleteBatiment() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);
        batimentSearchRepository.save(batiment);
        int databaseSizeBeforeDelete = batimentRepository.findAll().size();

        // Get the batiment
        restBatimentMockMvc.perform(delete("/api/batiments/{id}", batiment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean batimentExistsInEs = batimentSearchRepository.exists(batiment.getId());
        assertThat(batimentExistsInEs).isFalse();

        // Validate the database is empty
        List<Batiment> batiments = batimentRepository.findAll();
        assertThat(batiments).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBatiment() throws Exception {
        // Initialize the database
        batimentRepository.saveAndFlush(batiment);
        batimentSearchRepository.save(batiment);

        // Search the batiment
        restBatimentMockMvc.perform(get("/api/_search/batiments?query=id:" + batiment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batiment.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION.toString())));
    }
}
