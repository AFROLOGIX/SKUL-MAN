package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Pension;
import com.afrologix.skulman.repository.PensionRepository;
import com.afrologix.skulman.repository.search.PensionSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PensionResource REST controller.
 *
 * @see PensionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class PensionResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO = 10;
    private static final Integer UPDATED_NUMERO = 9;

    private static final LocalDate DEFAULT_DATE_DEB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private PensionRepository pensionRepository;

    @Inject
    private PensionSearchRepository pensionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPensionMockMvc;

    private Pension pension;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PensionResource pensionResource = new PensionResource();
        ReflectionTestUtils.setField(pensionResource, "pensionSearchRepository", pensionSearchRepository);
        ReflectionTestUtils.setField(pensionResource, "pensionRepository", pensionRepository);
        this.restPensionMockMvc = MockMvcBuilders.standaloneSetup(pensionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pensionSearchRepository.deleteAll();
        pension = new Pension();
        pension.setCode(DEFAULT_CODE);
        pension.setLibelleFr(DEFAULT_LIBELLE_FR);
        pension.setLibelleEn(DEFAULT_LIBELLE_EN);
        pension.setNumero(DEFAULT_NUMERO);
        pension.setDateDeb(DEFAULT_DATE_DEB);
        pension.setDateFin(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    public void createPension() throws Exception {
        int databaseSizeBeforeCreate = pensionRepository.findAll().size();

        // Create the Pension

        restPensionMockMvc.perform(post("/api/pensions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pension)))
                .andExpect(status().isCreated());

        // Validate the Pension in the database
        List<Pension> pensions = pensionRepository.findAll();
        assertThat(pensions).hasSize(databaseSizeBeforeCreate + 1);
        Pension testPension = pensions.get(pensions.size() - 1);
        assertThat(testPension.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPension.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testPension.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testPension.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testPension.getDateDeb()).isEqualTo(DEFAULT_DATE_DEB);
        assertThat(testPension.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);

        // Validate the Pension in ElasticSearch
        Pension pensionEs = pensionSearchRepository.findOne(testPension.getId());
        assertThat(pensionEs).isEqualToComparingFieldByField(testPension);
    }

    @Test
    @Transactional
    public void getAllPensions() throws Exception {
        // Initialize the database
        pensionRepository.saveAndFlush(pension);

        // Get all the pensions
        restPensionMockMvc.perform(get("/api/pensions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pension.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
                .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    public void getPension() throws Exception {
        // Initialize the database
        pensionRepository.saveAndFlush(pension);

        // Get the pension
        restPensionMockMvc.perform(get("/api/pensions/{id}", pension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pension.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.dateDeb").value(DEFAULT_DATE_DEB.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPension() throws Exception {
        // Get the pension
        restPensionMockMvc.perform(get("/api/pensions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePension() throws Exception {
        // Initialize the database
        pensionRepository.saveAndFlush(pension);
        pensionSearchRepository.save(pension);
        int databaseSizeBeforeUpdate = pensionRepository.findAll().size();

        // Update the pension
        Pension updatedPension = new Pension();
        updatedPension.setId(pension.getId());
        updatedPension.setCode(UPDATED_CODE);
        updatedPension.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedPension.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedPension.setNumero(UPDATED_NUMERO);
        updatedPension.setDateDeb(UPDATED_DATE_DEB);
        updatedPension.setDateFin(UPDATED_DATE_FIN);

        restPensionMockMvc.perform(put("/api/pensions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPension)))
                .andExpect(status().isOk());

        // Validate the Pension in the database
        List<Pension> pensions = pensionRepository.findAll();
        assertThat(pensions).hasSize(databaseSizeBeforeUpdate);
        Pension testPension = pensions.get(pensions.size() - 1);
        assertThat(testPension.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPension.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testPension.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testPension.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPension.getDateDeb()).isEqualTo(UPDATED_DATE_DEB);
        assertThat(testPension.getDateFin()).isEqualTo(UPDATED_DATE_FIN);

        // Validate the Pension in ElasticSearch
        Pension pensionEs = pensionSearchRepository.findOne(testPension.getId());
        assertThat(pensionEs).isEqualToComparingFieldByField(testPension);
    }

    @Test
    @Transactional
    public void deletePension() throws Exception {
        // Initialize the database
        pensionRepository.saveAndFlush(pension);
        pensionSearchRepository.save(pension);
        int databaseSizeBeforeDelete = pensionRepository.findAll().size();

        // Get the pension
        restPensionMockMvc.perform(delete("/api/pensions/{id}", pension.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean pensionExistsInEs = pensionSearchRepository.exists(pension.getId());
        assertThat(pensionExistsInEs).isFalse();

        // Validate the database is empty
        List<Pension> pensions = pensionRepository.findAll();
        assertThat(pensions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPension() throws Exception {
        // Initialize the database
        pensionRepository.saveAndFlush(pension);
        pensionSearchRepository.save(pension);

        // Search the pension
        restPensionMockMvc.perform(get("/api/_search/pensions?query=id:" + pension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pension.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }
}
