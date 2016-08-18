package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Jour;
import com.afrologix.skulman.repository.JourRepository;
import com.afrologix.skulman.repository.search.JourSearchRepository;

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
 * Test class for the JourResource REST controller.
 *
 * @see JourResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class JourResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private JourRepository jourRepository;

    @Inject
    private JourSearchRepository jourSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJourMockMvc;

    private Jour jour;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JourResource jourResource = new JourResource();
        ReflectionTestUtils.setField(jourResource, "jourSearchRepository", jourSearchRepository);
        ReflectionTestUtils.setField(jourResource, "jourRepository", jourRepository);
        this.restJourMockMvc = MockMvcBuilders.standaloneSetup(jourResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        jourSearchRepository.deleteAll();
        jour = new Jour();
        jour.setCode(DEFAULT_CODE);
        jour.setLibelleFr(DEFAULT_LIBELLE_FR);
        jour.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createJour() throws Exception {
        int databaseSizeBeforeCreate = jourRepository.findAll().size();

        // Create the Jour

        restJourMockMvc.perform(post("/api/jours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jour)))
                .andExpect(status().isCreated());

        // Validate the Jour in the database
        List<Jour> jours = jourRepository.findAll();
        assertThat(jours).hasSize(databaseSizeBeforeCreate + 1);
        Jour testJour = jours.get(jours.size() - 1);
        assertThat(testJour.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testJour.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testJour.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Jour in ElasticSearch
        Jour jourEs = jourSearchRepository.findOne(testJour.getId());
        assertThat(jourEs).isEqualToComparingFieldByField(testJour);
    }

    @Test
    @Transactional
    public void getAllJours() throws Exception {
        // Initialize the database
        jourRepository.saveAndFlush(jour);

        // Get all the jours
        restJourMockMvc.perform(get("/api/jours?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jour.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getJour() throws Exception {
        // Initialize the database
        jourRepository.saveAndFlush(jour);

        // Get the jour
        restJourMockMvc.perform(get("/api/jours/{id}", jour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jour.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJour() throws Exception {
        // Get the jour
        restJourMockMvc.perform(get("/api/jours/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJour() throws Exception {
        // Initialize the database
        jourRepository.saveAndFlush(jour);
        jourSearchRepository.save(jour);
        int databaseSizeBeforeUpdate = jourRepository.findAll().size();

        // Update the jour
        Jour updatedJour = new Jour();
        updatedJour.setId(jour.getId());
        updatedJour.setCode(UPDATED_CODE);
        updatedJour.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedJour.setLibelleEn(UPDATED_LIBELLE_EN);

        restJourMockMvc.perform(put("/api/jours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedJour)))
                .andExpect(status().isOk());

        // Validate the Jour in the database
        List<Jour> jours = jourRepository.findAll();
        assertThat(jours).hasSize(databaseSizeBeforeUpdate);
        Jour testJour = jours.get(jours.size() - 1);
        assertThat(testJour.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testJour.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testJour.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Jour in ElasticSearch
        Jour jourEs = jourSearchRepository.findOne(testJour.getId());
        assertThat(jourEs).isEqualToComparingFieldByField(testJour);
    }

    @Test
    @Transactional
    public void deleteJour() throws Exception {
        // Initialize the database
        jourRepository.saveAndFlush(jour);
        jourSearchRepository.save(jour);
        int databaseSizeBeforeDelete = jourRepository.findAll().size();

        // Get the jour
        restJourMockMvc.perform(delete("/api/jours/{id}", jour.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean jourExistsInEs = jourSearchRepository.exists(jour.getId());
        assertThat(jourExistsInEs).isFalse();

        // Validate the database is empty
        List<Jour> jours = jourRepository.findAll();
        assertThat(jours).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJour() throws Exception {
        // Initialize the database
        jourRepository.saveAndFlush(jour);
        jourSearchRepository.save(jour);

        // Search the jour
        restJourMockMvc.perform(get("/api/_search/jours?query=id:" + jour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jour.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
