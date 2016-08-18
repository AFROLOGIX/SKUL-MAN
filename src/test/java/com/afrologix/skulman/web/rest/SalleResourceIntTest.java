package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Salle;
import com.afrologix.skulman.repository.SalleRepository;
import com.afrologix.skulman.repository.search.SalleSearchRepository;

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
 * Test class for the SalleResource REST controller.
 *
 * @see SalleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class SalleResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SalleRepository salleRepository;

    @Inject
    private SalleSearchRepository salleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSalleMockMvc;

    private Salle salle;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SalleResource salleResource = new SalleResource();
        ReflectionTestUtils.setField(salleResource, "salleSearchRepository", salleSearchRepository);
        ReflectionTestUtils.setField(salleResource, "salleRepository", salleRepository);
        this.restSalleMockMvc = MockMvcBuilders.standaloneSetup(salleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        salleSearchRepository.deleteAll();
        salle = new Salle();
        salle.setCode(DEFAULT_CODE);
        salle.setLibelleFr(DEFAULT_LIBELLE_FR);
        salle.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createSalle() throws Exception {
        int databaseSizeBeforeCreate = salleRepository.findAll().size();

        // Create the Salle

        restSalleMockMvc.perform(post("/api/salles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(salle)))
                .andExpect(status().isCreated());

        // Validate the Salle in the database
        List<Salle> salles = salleRepository.findAll();
        assertThat(salles).hasSize(databaseSizeBeforeCreate + 1);
        Salle testSalle = salles.get(salles.size() - 1);
        assertThat(testSalle.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSalle.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testSalle.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Salle in ElasticSearch
        Salle salleEs = salleSearchRepository.findOne(testSalle.getId());
        assertThat(salleEs).isEqualToComparingFieldByField(testSalle);
    }

    @Test
    @Transactional
    public void getAllSalles() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);

        // Get all the salles
        restSalleMockMvc.perform(get("/api/salles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(salle.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getSalle() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);

        // Get the salle
        restSalleMockMvc.perform(get("/api/salles/{id}", salle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(salle.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSalle() throws Exception {
        // Get the salle
        restSalleMockMvc.perform(get("/api/salles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalle() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);
        salleSearchRepository.save(salle);
        int databaseSizeBeforeUpdate = salleRepository.findAll().size();

        // Update the salle
        Salle updatedSalle = new Salle();
        updatedSalle.setId(salle.getId());
        updatedSalle.setCode(UPDATED_CODE);
        updatedSalle.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedSalle.setLibelleEn(UPDATED_LIBELLE_EN);

        restSalleMockMvc.perform(put("/api/salles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSalle)))
                .andExpect(status().isOk());

        // Validate the Salle in the database
        List<Salle> salles = salleRepository.findAll();
        assertThat(salles).hasSize(databaseSizeBeforeUpdate);
        Salle testSalle = salles.get(salles.size() - 1);
        assertThat(testSalle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSalle.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testSalle.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Salle in ElasticSearch
        Salle salleEs = salleSearchRepository.findOne(testSalle.getId());
        assertThat(salleEs).isEqualToComparingFieldByField(testSalle);
    }

    @Test
    @Transactional
    public void deleteSalle() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);
        salleSearchRepository.save(salle);
        int databaseSizeBeforeDelete = salleRepository.findAll().size();

        // Get the salle
        restSalleMockMvc.perform(delete("/api/salles/{id}", salle.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean salleExistsInEs = salleSearchRepository.exists(salle.getId());
        assertThat(salleExistsInEs).isFalse();

        // Validate the database is empty
        List<Salle> salles = salleRepository.findAll();
        assertThat(salles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSalle() throws Exception {
        // Initialize the database
        salleRepository.saveAndFlush(salle);
        salleSearchRepository.save(salle);

        // Search the salle
        restSalleMockMvc.perform(get("/api/_search/salles?query=id:" + salle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salle.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
