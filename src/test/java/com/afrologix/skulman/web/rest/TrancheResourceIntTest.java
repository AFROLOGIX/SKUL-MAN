package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Tranche;
import com.afrologix.skulman.repository.TrancheRepository;
import com.afrologix.skulman.repository.search.TrancheSearchRepository;

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
 * Test class for the TrancheResource REST controller.
 *
 * @see TrancheResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TrancheResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final LocalDate DEFAULT_DATE_DEB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private TrancheRepository trancheRepository;

    @Inject
    private TrancheSearchRepository trancheSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTrancheMockMvc;

    private Tranche tranche;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrancheResource trancheResource = new TrancheResource();
        ReflectionTestUtils.setField(trancheResource, "trancheSearchRepository", trancheSearchRepository);
        ReflectionTestUtils.setField(trancheResource, "trancheRepository", trancheRepository);
        this.restTrancheMockMvc = MockMvcBuilders.standaloneSetup(trancheResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        trancheSearchRepository.deleteAll();
        tranche = new Tranche();
        tranche.setCode(DEFAULT_CODE);
        tranche.setLibelleFr(DEFAULT_LIBELLE_FR);
        tranche.setLibelleEn(DEFAULT_LIBELLE_EN);
        tranche.setNumero(DEFAULT_NUMERO);
        tranche.setDateDeb(DEFAULT_DATE_DEB);
        tranche.setDateFin(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    public void createTranche() throws Exception {
        int databaseSizeBeforeCreate = trancheRepository.findAll().size();

        // Create the Tranche

        restTrancheMockMvc.perform(post("/api/tranches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tranche)))
                .andExpect(status().isCreated());

        // Validate the Tranche in the database
        List<Tranche> tranches = trancheRepository.findAll();
        assertThat(tranches).hasSize(databaseSizeBeforeCreate + 1);
        Tranche testTranche = tranches.get(tranches.size() - 1);
        assertThat(testTranche.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTranche.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testTranche.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testTranche.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testTranche.getDateDeb()).isEqualTo(DEFAULT_DATE_DEB);
        assertThat(testTranche.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);

        // Validate the Tranche in ElasticSearch
        Tranche trancheEs = trancheSearchRepository.findOne(testTranche.getId());
        assertThat(trancheEs).isEqualToComparingFieldByField(testTranche);
    }

    @Test
    @Transactional
    public void getAllTranches() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the tranches
        restTrancheMockMvc.perform(get("/api/tranches?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tranche.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
                .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    public void getTranche() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get the tranche
        restTrancheMockMvc.perform(get("/api/tranches/{id}", tranche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tranche.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.dateDeb").value(DEFAULT_DATE_DEB.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTranche() throws Exception {
        // Get the tranche
        restTrancheMockMvc.perform(get("/api/tranches/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranche() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);
        trancheSearchRepository.save(tranche);
        int databaseSizeBeforeUpdate = trancheRepository.findAll().size();

        // Update the tranche
        Tranche updatedTranche = new Tranche();
        updatedTranche.setId(tranche.getId());
        updatedTranche.setCode(UPDATED_CODE);
        updatedTranche.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedTranche.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedTranche.setNumero(UPDATED_NUMERO);
        updatedTranche.setDateDeb(UPDATED_DATE_DEB);
        updatedTranche.setDateFin(UPDATED_DATE_FIN);

        restTrancheMockMvc.perform(put("/api/tranches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTranche)))
                .andExpect(status().isOk());

        // Validate the Tranche in the database
        List<Tranche> tranches = trancheRepository.findAll();
        assertThat(tranches).hasSize(databaseSizeBeforeUpdate);
        Tranche testTranche = tranches.get(tranches.size() - 1);
        assertThat(testTranche.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTranche.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testTranche.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testTranche.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testTranche.getDateDeb()).isEqualTo(UPDATED_DATE_DEB);
        assertThat(testTranche.getDateFin()).isEqualTo(UPDATED_DATE_FIN);

        // Validate the Tranche in ElasticSearch
        Tranche trancheEs = trancheSearchRepository.findOne(testTranche.getId());
        assertThat(trancheEs).isEqualToComparingFieldByField(testTranche);
    }

    @Test
    @Transactional
    public void deleteTranche() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);
        trancheSearchRepository.save(tranche);
        int databaseSizeBeforeDelete = trancheRepository.findAll().size();

        // Get the tranche
        restTrancheMockMvc.perform(delete("/api/tranches/{id}", tranche.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean trancheExistsInEs = trancheSearchRepository.exists(tranche.getId());
        assertThat(trancheExistsInEs).isFalse();

        // Validate the database is empty
        List<Tranche> tranches = trancheRepository.findAll();
        assertThat(tranches).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTranche() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);
        trancheSearchRepository.save(tranche);

        // Search the tranche
        restTrancheMockMvc.perform(get("/api/_search/tranches?query=id:" + tranche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tranche.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }
}
