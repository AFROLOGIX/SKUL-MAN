package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Trimestre;
import com.afrologix.skulman.repository.TrimestreRepository;
import com.afrologix.skulman.repository.search.TrimestreSearchRepository;

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
 * Test class for the TrimestreResource REST controller.
 *
 * @see TrimestreResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TrimestreResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Inject
    private TrimestreRepository trimestreRepository;

    @Inject
    private TrimestreSearchRepository trimestreSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTrimestreMockMvc;

    private Trimestre trimestre;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrimestreResource trimestreResource = new TrimestreResource();
        ReflectionTestUtils.setField(trimestreResource, "trimestreSearchRepository", trimestreSearchRepository);
        ReflectionTestUtils.setField(trimestreResource, "trimestreRepository", trimestreRepository);
        this.restTrimestreMockMvc = MockMvcBuilders.standaloneSetup(trimestreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        trimestreSearchRepository.deleteAll();
        trimestre = new Trimestre();
        trimestre.setCode(DEFAULT_CODE);
        trimestre.setLibelleFr(DEFAULT_LIBELLE_FR);
        trimestre.setLibelleEn(DEFAULT_LIBELLE_EN);
        trimestre.setDateDeb(DEFAULT_DATE_DEB);
        trimestre.setDateFin(DEFAULT_DATE_FIN);
        trimestre.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        trimestre.setIsActive(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createTrimestre() throws Exception {
        int databaseSizeBeforeCreate = trimestreRepository.findAll().size();

        // Create the Trimestre

        restTrimestreMockMvc.perform(post("/api/trimestres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trimestre)))
                .andExpect(status().isCreated());

        // Validate the Trimestre in the database
        List<Trimestre> trimestres = trimestreRepository.findAll();
        assertThat(trimestres).hasSize(databaseSizeBeforeCreate + 1);
        Trimestre testTrimestre = trimestres.get(trimestres.size() - 1);
        assertThat(testTrimestre.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTrimestre.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testTrimestre.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testTrimestre.getDateDeb()).isEqualTo(DEFAULT_DATE_DEB);
        assertThat(testTrimestre.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testTrimestre.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testTrimestre.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the Trimestre in ElasticSearch
        Trimestre trimestreEs = trimestreSearchRepository.findOne(testTrimestre.getId());
        assertThat(trimestreEs).isEqualToComparingFieldByField(testTrimestre);
    }

    @Test
    @Transactional
    public void getAllTrimestres() throws Exception {
        // Initialize the database
        trimestreRepository.saveAndFlush(trimestre);

        // Get all the trimestres
        restTrimestreMockMvc.perform(get("/api/trimestres?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trimestre.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getTrimestre() throws Exception {
        // Initialize the database
        trimestreRepository.saveAndFlush(trimestre);

        // Get the trimestre
        restTrimestreMockMvc.perform(get("/api/trimestres/{id}", trimestre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(trimestre.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.dateDeb").value(DEFAULT_DATE_DEB.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTrimestre() throws Exception {
        // Get the trimestre
        restTrimestreMockMvc.perform(get("/api/trimestres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrimestre() throws Exception {
        // Initialize the database
        trimestreRepository.saveAndFlush(trimestre);
        trimestreSearchRepository.save(trimestre);
        int databaseSizeBeforeUpdate = trimestreRepository.findAll().size();

        // Update the trimestre
        Trimestre updatedTrimestre = new Trimestre();
        updatedTrimestre.setId(trimestre.getId());
        updatedTrimestre.setCode(UPDATED_CODE);
        updatedTrimestre.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedTrimestre.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedTrimestre.setDateDeb(UPDATED_DATE_DEB);
        updatedTrimestre.setDateFin(UPDATED_DATE_FIN);
        updatedTrimestre.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedTrimestre.setIsActive(UPDATED_IS_ACTIVE);

        restTrimestreMockMvc.perform(put("/api/trimestres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTrimestre)))
                .andExpect(status().isOk());

        // Validate the Trimestre in the database
        List<Trimestre> trimestres = trimestreRepository.findAll();
        assertThat(trimestres).hasSize(databaseSizeBeforeUpdate);
        Trimestre testTrimestre = trimestres.get(trimestres.size() - 1);
        assertThat(testTrimestre.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTrimestre.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testTrimestre.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testTrimestre.getDateDeb()).isEqualTo(UPDATED_DATE_DEB);
        assertThat(testTrimestre.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testTrimestre.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testTrimestre.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the Trimestre in ElasticSearch
        Trimestre trimestreEs = trimestreSearchRepository.findOne(testTrimestre.getId());
        assertThat(trimestreEs).isEqualToComparingFieldByField(testTrimestre);
    }

    @Test
    @Transactional
    public void deleteTrimestre() throws Exception {
        // Initialize the database
        trimestreRepository.saveAndFlush(trimestre);
        trimestreSearchRepository.save(trimestre);
        int databaseSizeBeforeDelete = trimestreRepository.findAll().size();

        // Get the trimestre
        restTrimestreMockMvc.perform(delete("/api/trimestres/{id}", trimestre.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean trimestreExistsInEs = trimestreSearchRepository.exists(trimestre.getId());
        assertThat(trimestreExistsInEs).isFalse();

        // Validate the database is empty
        List<Trimestre> trimestres = trimestreRepository.findAll();
        assertThat(trimestres).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTrimestre() throws Exception {
        // Initialize the database
        trimestreRepository.saveAndFlush(trimestre);
        trimestreSearchRepository.save(trimestre);

        // Search the trimestre
        restTrimestreMockMvc.perform(get("/api/_search/trimestres?query=id:" + trimestre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trimestre.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
