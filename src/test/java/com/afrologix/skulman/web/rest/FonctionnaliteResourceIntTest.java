package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Fonctionnalite;
import com.afrologix.skulman.repository.FonctionnaliteRepository;
import com.afrologix.skulman.repository.search.FonctionnaliteSearchRepository;

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
 * Test class for the FonctionnaliteResource REST controller.
 *
 * @see FonctionnaliteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class FonctionnaliteResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private FonctionnaliteRepository fonctionnaliteRepository;

    @Inject
    private FonctionnaliteSearchRepository fonctionnaliteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFonctionnaliteMockMvc;

    private Fonctionnalite fonctionnalite;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FonctionnaliteResource fonctionnaliteResource = new FonctionnaliteResource();
        ReflectionTestUtils.setField(fonctionnaliteResource, "fonctionnaliteSearchRepository", fonctionnaliteSearchRepository);
        ReflectionTestUtils.setField(fonctionnaliteResource, "fonctionnaliteRepository", fonctionnaliteRepository);
        this.restFonctionnaliteMockMvc = MockMvcBuilders.standaloneSetup(fonctionnaliteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fonctionnaliteSearchRepository.deleteAll();
        fonctionnalite = new Fonctionnalite();
        fonctionnalite.setCode(DEFAULT_CODE);
        fonctionnalite.setLibelleFr(DEFAULT_LIBELLE_FR);
        fonctionnalite.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createFonctionnalite() throws Exception {
        int databaseSizeBeforeCreate = fonctionnaliteRepository.findAll().size();

        // Create the Fonctionnalite

        restFonctionnaliteMockMvc.perform(post("/api/fonctionnalites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonctionnalite)))
                .andExpect(status().isCreated());

        // Validate the Fonctionnalite in the database
        List<Fonctionnalite> fonctionnalites = fonctionnaliteRepository.findAll();
        assertThat(fonctionnalites).hasSize(databaseSizeBeforeCreate + 1);
        Fonctionnalite testFonctionnalite = fonctionnalites.get(fonctionnalites.size() - 1);
        assertThat(testFonctionnalite.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFonctionnalite.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testFonctionnalite.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Fonctionnalite in ElasticSearch
        Fonctionnalite fonctionnaliteEs = fonctionnaliteSearchRepository.findOne(testFonctionnalite.getId());
        assertThat(fonctionnaliteEs).isEqualToComparingFieldByField(testFonctionnalite);
    }

    @Test
    @Transactional
    public void getAllFonctionnalites() throws Exception {
        // Initialize the database
        fonctionnaliteRepository.saveAndFlush(fonctionnalite);

        // Get all the fonctionnalites
        restFonctionnaliteMockMvc.perform(get("/api/fonctionnalites?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fonctionnalite.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getFonctionnalite() throws Exception {
        // Initialize the database
        fonctionnaliteRepository.saveAndFlush(fonctionnalite);

        // Get the fonctionnalite
        restFonctionnaliteMockMvc.perform(get("/api/fonctionnalites/{id}", fonctionnalite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fonctionnalite.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFonctionnalite() throws Exception {
        // Get the fonctionnalite
        restFonctionnaliteMockMvc.perform(get("/api/fonctionnalites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFonctionnalite() throws Exception {
        // Initialize the database
        fonctionnaliteRepository.saveAndFlush(fonctionnalite);
        fonctionnaliteSearchRepository.save(fonctionnalite);
        int databaseSizeBeforeUpdate = fonctionnaliteRepository.findAll().size();

        // Update the fonctionnalite
        Fonctionnalite updatedFonctionnalite = new Fonctionnalite();
        updatedFonctionnalite.setId(fonctionnalite.getId());
        updatedFonctionnalite.setCode(UPDATED_CODE);
        updatedFonctionnalite.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedFonctionnalite.setLibelleEn(UPDATED_LIBELLE_EN);

        restFonctionnaliteMockMvc.perform(put("/api/fonctionnalites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFonctionnalite)))
                .andExpect(status().isOk());

        // Validate the Fonctionnalite in the database
        List<Fonctionnalite> fonctionnalites = fonctionnaliteRepository.findAll();
        assertThat(fonctionnalites).hasSize(databaseSizeBeforeUpdate);
        Fonctionnalite testFonctionnalite = fonctionnalites.get(fonctionnalites.size() - 1);
        assertThat(testFonctionnalite.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFonctionnalite.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testFonctionnalite.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Fonctionnalite in ElasticSearch
        Fonctionnalite fonctionnaliteEs = fonctionnaliteSearchRepository.findOne(testFonctionnalite.getId());
        assertThat(fonctionnaliteEs).isEqualToComparingFieldByField(testFonctionnalite);
    }

    @Test
    @Transactional
    public void deleteFonctionnalite() throws Exception {
        // Initialize the database
        fonctionnaliteRepository.saveAndFlush(fonctionnalite);
        fonctionnaliteSearchRepository.save(fonctionnalite);
        int databaseSizeBeforeDelete = fonctionnaliteRepository.findAll().size();

        // Get the fonctionnalite
        restFonctionnaliteMockMvc.perform(delete("/api/fonctionnalites/{id}", fonctionnalite.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean fonctionnaliteExistsInEs = fonctionnaliteSearchRepository.exists(fonctionnalite.getId());
        assertThat(fonctionnaliteExistsInEs).isFalse();

        // Validate the database is empty
        List<Fonctionnalite> fonctionnalites = fonctionnaliteRepository.findAll();
        assertThat(fonctionnalites).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFonctionnalite() throws Exception {
        // Initialize the database
        fonctionnaliteRepository.saveAndFlush(fonctionnalite);
        fonctionnaliteSearchRepository.save(fonctionnalite);

        // Search the fonctionnalite
        restFonctionnaliteMockMvc.perform(get("/api/_search/fonctionnalites?query=id:" + fonctionnalite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fonctionnalite.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
