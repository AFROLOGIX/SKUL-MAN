package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Niveau;
import com.afrologix.skulman.repository.NiveauRepository;
import com.afrologix.skulman.repository.search.NiveauSearchRepository;

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
 * Test class for the NiveauResource REST controller.
 *
 * @see NiveauResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class NiveauResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private NiveauRepository niveauRepository;

    @Inject
    private NiveauSearchRepository niveauSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNiveauMockMvc;

    private Niveau niveau;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NiveauResource niveauResource = new NiveauResource();
        ReflectionTestUtils.setField(niveauResource, "niveauSearchRepository", niveauSearchRepository);
        ReflectionTestUtils.setField(niveauResource, "niveauRepository", niveauRepository);
        this.restNiveauMockMvc = MockMvcBuilders.standaloneSetup(niveauResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        niveauSearchRepository.deleteAll();
        niveau = new Niveau();
        niveau.setCode(DEFAULT_CODE);
        niveau.setLibelleFr(DEFAULT_LIBELLE_FR);
        niveau.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createNiveau() throws Exception {
        int databaseSizeBeforeCreate = niveauRepository.findAll().size();

        // Create the Niveau

        restNiveauMockMvc.perform(post("/api/niveaus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(niveau)))
                .andExpect(status().isCreated());

        // Validate the Niveau in the database
        List<Niveau> niveaus = niveauRepository.findAll();
        assertThat(niveaus).hasSize(databaseSizeBeforeCreate + 1);
        Niveau testNiveau = niveaus.get(niveaus.size() - 1);
        assertThat(testNiveau.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testNiveau.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testNiveau.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Niveau in ElasticSearch
        Niveau niveauEs = niveauSearchRepository.findOne(testNiveau.getId());
        assertThat(niveauEs).isEqualToComparingFieldByField(testNiveau);
    }

    @Test
    @Transactional
    public void getAllNiveaus() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        // Get all the niveaus
        restNiveauMockMvc.perform(get("/api/niveaus?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(niveau.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getNiveau() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        // Get the niveau
        restNiveauMockMvc.perform(get("/api/niveaus/{id}", niveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(niveau.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNiveau() throws Exception {
        // Get the niveau
        restNiveauMockMvc.perform(get("/api/niveaus/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNiveau() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);
        niveauSearchRepository.save(niveau);
        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();

        // Update the niveau
        Niveau updatedNiveau = new Niveau();
        updatedNiveau.setId(niveau.getId());
        updatedNiveau.setCode(UPDATED_CODE);
        updatedNiveau.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedNiveau.setLibelleEn(UPDATED_LIBELLE_EN);

        restNiveauMockMvc.perform(put("/api/niveaus")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNiveau)))
                .andExpect(status().isOk());

        // Validate the Niveau in the database
        List<Niveau> niveaus = niveauRepository.findAll();
        assertThat(niveaus).hasSize(databaseSizeBeforeUpdate);
        Niveau testNiveau = niveaus.get(niveaus.size() - 1);
        assertThat(testNiveau.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testNiveau.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testNiveau.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Niveau in ElasticSearch
        Niveau niveauEs = niveauSearchRepository.findOne(testNiveau.getId());
        assertThat(niveauEs).isEqualToComparingFieldByField(testNiveau);
    }

    @Test
    @Transactional
    public void deleteNiveau() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);
        niveauSearchRepository.save(niveau);
        int databaseSizeBeforeDelete = niveauRepository.findAll().size();

        // Get the niveau
        restNiveauMockMvc.perform(delete("/api/niveaus/{id}", niveau.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean niveauExistsInEs = niveauSearchRepository.exists(niveau.getId());
        assertThat(niveauExistsInEs).isFalse();

        // Validate the database is empty
        List<Niveau> niveaus = niveauRepository.findAll();
        assertThat(niveaus).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNiveau() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);
        niveauSearchRepository.save(niveau);

        // Search the niveau
        restNiveauMockMvc.perform(get("/api/_search/niveaus?query=id:" + niveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(niveau.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
