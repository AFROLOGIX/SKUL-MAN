package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.RegimePension;
import com.afrologix.skulman.repository.RegimePensionRepository;
import com.afrologix.skulman.repository.search.RegimePensionSearchRepository;

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
 * Test class for the RegimePensionResource REST controller.
 *
 * @see RegimePensionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class RegimePensionResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_NB_TOTAL_TRANCHES = 10;
    private static final Integer UPDATED_NB_TOTAL_TRANCHES = 9;

    @Inject
    private RegimePensionRepository regimePensionRepository;

    @Inject
    private RegimePensionSearchRepository regimePensionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRegimePensionMockMvc;

    private RegimePension regimePension;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegimePensionResource regimePensionResource = new RegimePensionResource();
        ReflectionTestUtils.setField(regimePensionResource, "regimePensionSearchRepository", regimePensionSearchRepository);
        ReflectionTestUtils.setField(regimePensionResource, "regimePensionRepository", regimePensionRepository);
        this.restRegimePensionMockMvc = MockMvcBuilders.standaloneSetup(regimePensionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        regimePensionSearchRepository.deleteAll();
        regimePension = new RegimePension();
        regimePension.setCode(DEFAULT_CODE);
        regimePension.setLibelleFr(DEFAULT_LIBELLE_FR);
        regimePension.setLibelleEn(DEFAULT_LIBELLE_EN);
        regimePension.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        regimePension.setNbTotalTranches(DEFAULT_NB_TOTAL_TRANCHES);
    }

    @Test
    @Transactional
    public void createRegimePension() throws Exception {
        int databaseSizeBeforeCreate = regimePensionRepository.findAll().size();

        // Create the RegimePension

        restRegimePensionMockMvc.perform(post("/api/regime-pensions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(regimePension)))
                .andExpect(status().isCreated());

        // Validate the RegimePension in the database
        List<RegimePension> regimePensions = regimePensionRepository.findAll();
        assertThat(regimePensions).hasSize(databaseSizeBeforeCreate + 1);
        RegimePension testRegimePension = regimePensions.get(regimePensions.size() - 1);
        assertThat(testRegimePension.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRegimePension.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testRegimePension.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testRegimePension.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testRegimePension.getNbTotalTranches()).isEqualTo(DEFAULT_NB_TOTAL_TRANCHES);

        // Validate the RegimePension in ElasticSearch
        RegimePension regimePensionEs = regimePensionSearchRepository.findOne(testRegimePension.getId());
        assertThat(regimePensionEs).isEqualToComparingFieldByField(testRegimePension);
    }

    @Test
    @Transactional
    public void getAllRegimePensions() throws Exception {
        // Initialize the database
        regimePensionRepository.saveAndFlush(regimePension);

        // Get all the regimePensions
        restRegimePensionMockMvc.perform(get("/api/regime-pensions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(regimePension.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].nbTotalTranches").value(hasItem(DEFAULT_NB_TOTAL_TRANCHES)));
    }

    @Test
    @Transactional
    public void getRegimePension() throws Exception {
        // Initialize the database
        regimePensionRepository.saveAndFlush(regimePension);

        // Get the regimePension
        restRegimePensionMockMvc.perform(get("/api/regime-pensions/{id}", regimePension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(regimePension.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.nbTotalTranches").value(DEFAULT_NB_TOTAL_TRANCHES));
    }

    @Test
    @Transactional
    public void getNonExistingRegimePension() throws Exception {
        // Get the regimePension
        restRegimePensionMockMvc.perform(get("/api/regime-pensions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegimePension() throws Exception {
        // Initialize the database
        regimePensionRepository.saveAndFlush(regimePension);
        regimePensionSearchRepository.save(regimePension);
        int databaseSizeBeforeUpdate = regimePensionRepository.findAll().size();

        // Update the regimePension
        RegimePension updatedRegimePension = new RegimePension();
        updatedRegimePension.setId(regimePension.getId());
        updatedRegimePension.setCode(UPDATED_CODE);
        updatedRegimePension.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedRegimePension.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedRegimePension.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedRegimePension.setNbTotalTranches(UPDATED_NB_TOTAL_TRANCHES);

        restRegimePensionMockMvc.perform(put("/api/regime-pensions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRegimePension)))
                .andExpect(status().isOk());

        // Validate the RegimePension in the database
        List<RegimePension> regimePensions = regimePensionRepository.findAll();
        assertThat(regimePensions).hasSize(databaseSizeBeforeUpdate);
        RegimePension testRegimePension = regimePensions.get(regimePensions.size() - 1);
        assertThat(testRegimePension.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRegimePension.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testRegimePension.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testRegimePension.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testRegimePension.getNbTotalTranches()).isEqualTo(UPDATED_NB_TOTAL_TRANCHES);

        // Validate the RegimePension in ElasticSearch
        RegimePension regimePensionEs = regimePensionSearchRepository.findOne(testRegimePension.getId());
        assertThat(regimePensionEs).isEqualToComparingFieldByField(testRegimePension);
    }

    @Test
    @Transactional
    public void deleteRegimePension() throws Exception {
        // Initialize the database
        regimePensionRepository.saveAndFlush(regimePension);
        regimePensionSearchRepository.save(regimePension);
        int databaseSizeBeforeDelete = regimePensionRepository.findAll().size();

        // Get the regimePension
        restRegimePensionMockMvc.perform(delete("/api/regime-pensions/{id}", regimePension.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean regimePensionExistsInEs = regimePensionSearchRepository.exists(regimePension.getId());
        assertThat(regimePensionExistsInEs).isFalse();

        // Validate the database is empty
        List<RegimePension> regimePensions = regimePensionRepository.findAll();
        assertThat(regimePensions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRegimePension() throws Exception {
        // Initialize the database
        regimePensionRepository.saveAndFlush(regimePension);
        regimePensionSearchRepository.save(regimePension);

        // Search the regimePension
        restRegimePensionMockMvc.perform(get("/api/_search/regime-pensions?query=id:" + regimePension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regimePension.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].nbTotalTranches").value(hasItem(DEFAULT_NB_TOTAL_TRANCHES)));
    }
}
