package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.PaiementPersonnel;
import com.afrologix.skulman.repository.PaiementPersonnelRepository;
import com.afrologix.skulman.repository.search.PaiementPersonnelSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PaiementPersonnelResource REST controller.
 *
 * @see PaiementPersonnelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class PaiementPersonnelResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DETTE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DETTE = new BigDecimal(2);
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PERIODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PERIODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_UPDATE_BY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_UPDATE_BY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_AT_STR = dateTimeFormatter.format(DEFAULT_CREATE_AT);

    private static final ZonedDateTime DEFAULT_UPDATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATE_AT_STR = dateTimeFormatter.format(DEFAULT_UPDATE_AT);

    @Inject
    private PaiementPersonnelRepository paiementPersonnelRepository;

    @Inject
    private PaiementPersonnelSearchRepository paiementPersonnelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPaiementPersonnelMockMvc;

    private PaiementPersonnel paiementPersonnel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaiementPersonnelResource paiementPersonnelResource = new PaiementPersonnelResource();
        ReflectionTestUtils.setField(paiementPersonnelResource, "paiementPersonnelSearchRepository", paiementPersonnelSearchRepository);
        ReflectionTestUtils.setField(paiementPersonnelResource, "paiementPersonnelRepository", paiementPersonnelRepository);
        this.restPaiementPersonnelMockMvc = MockMvcBuilders.standaloneSetup(paiementPersonnelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        paiementPersonnelSearchRepository.deleteAll();
        paiementPersonnel = new PaiementPersonnel();
        paiementPersonnel.setMontant(DEFAULT_MONTANT);
        paiementPersonnel.setDette(DEFAULT_DETTE);
        paiementPersonnel.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        paiementPersonnel.setPeriode(DEFAULT_PERIODE);
        paiementPersonnel.setCreateBy(DEFAULT_CREATE_BY);
        paiementPersonnel.setUpdateBy(DEFAULT_UPDATE_BY);
        paiementPersonnel.setCreateAt(DEFAULT_CREATE_AT);
        paiementPersonnel.setUpdateAt(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createPaiementPersonnel() throws Exception {
        int databaseSizeBeforeCreate = paiementPersonnelRepository.findAll().size();

        // Create the PaiementPersonnel

        restPaiementPersonnelMockMvc.perform(post("/api/paiement-personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paiementPersonnel)))
                .andExpect(status().isCreated());

        // Validate the PaiementPersonnel in the database
        List<PaiementPersonnel> paiementPersonnels = paiementPersonnelRepository.findAll();
        assertThat(paiementPersonnels).hasSize(databaseSizeBeforeCreate + 1);
        PaiementPersonnel testPaiementPersonnel = paiementPersonnels.get(paiementPersonnels.size() - 1);
        assertThat(testPaiementPersonnel.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testPaiementPersonnel.getDette()).isEqualTo(DEFAULT_DETTE);
        assertThat(testPaiementPersonnel.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testPaiementPersonnel.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testPaiementPersonnel.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testPaiementPersonnel.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testPaiementPersonnel.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testPaiementPersonnel.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the PaiementPersonnel in ElasticSearch
        PaiementPersonnel paiementPersonnelEs = paiementPersonnelSearchRepository.findOne(testPaiementPersonnel.getId());
        assertThat(paiementPersonnelEs).isEqualToComparingFieldByField(testPaiementPersonnel);
    }

    @Test
    @Transactional
    public void getAllPaiementPersonnels() throws Exception {
        // Initialize the database
        paiementPersonnelRepository.saveAndFlush(paiementPersonnel);

        // Get all the paiementPersonnels
        restPaiementPersonnelMockMvc.perform(get("/api/paiement-personnels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(paiementPersonnel.getId().intValue())))
                .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
                .andExpect(jsonPath("$.[*].dette").value(hasItem(DEFAULT_DETTE.intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
                .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }

    @Test
    @Transactional
    public void getPaiementPersonnel() throws Exception {
        // Initialize the database
        paiementPersonnelRepository.saveAndFlush(paiementPersonnel);

        // Get the paiementPersonnel
        restPaiementPersonnelMockMvc.perform(get("/api/paiement-personnels/{id}", paiementPersonnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(paiementPersonnel.getId().intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.dette").value(DEFAULT_DETTE.intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT_STR))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPaiementPersonnel() throws Exception {
        // Get the paiementPersonnel
        restPaiementPersonnelMockMvc.perform(get("/api/paiement-personnels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaiementPersonnel() throws Exception {
        // Initialize the database
        paiementPersonnelRepository.saveAndFlush(paiementPersonnel);
        paiementPersonnelSearchRepository.save(paiementPersonnel);
        int databaseSizeBeforeUpdate = paiementPersonnelRepository.findAll().size();

        // Update the paiementPersonnel
        PaiementPersonnel updatedPaiementPersonnel = new PaiementPersonnel();
        updatedPaiementPersonnel.setId(paiementPersonnel.getId());
        updatedPaiementPersonnel.setMontant(UPDATED_MONTANT);
        updatedPaiementPersonnel.setDette(UPDATED_DETTE);
        updatedPaiementPersonnel.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedPaiementPersonnel.setPeriode(UPDATED_PERIODE);
        updatedPaiementPersonnel.setCreateBy(UPDATED_CREATE_BY);
        updatedPaiementPersonnel.setUpdateBy(UPDATED_UPDATE_BY);
        updatedPaiementPersonnel.setCreateAt(UPDATED_CREATE_AT);
        updatedPaiementPersonnel.setUpdateAt(UPDATED_UPDATE_AT);

        restPaiementPersonnelMockMvc.perform(put("/api/paiement-personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPaiementPersonnel)))
                .andExpect(status().isOk());

        // Validate the PaiementPersonnel in the database
        List<PaiementPersonnel> paiementPersonnels = paiementPersonnelRepository.findAll();
        assertThat(paiementPersonnels).hasSize(databaseSizeBeforeUpdate);
        PaiementPersonnel testPaiementPersonnel = paiementPersonnels.get(paiementPersonnels.size() - 1);
        assertThat(testPaiementPersonnel.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testPaiementPersonnel.getDette()).isEqualTo(UPDATED_DETTE);
        assertThat(testPaiementPersonnel.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testPaiementPersonnel.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testPaiementPersonnel.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testPaiementPersonnel.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testPaiementPersonnel.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testPaiementPersonnel.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the PaiementPersonnel in ElasticSearch
        PaiementPersonnel paiementPersonnelEs = paiementPersonnelSearchRepository.findOne(testPaiementPersonnel.getId());
        assertThat(paiementPersonnelEs).isEqualToComparingFieldByField(testPaiementPersonnel);
    }

    @Test
    @Transactional
    public void deletePaiementPersonnel() throws Exception {
        // Initialize the database
        paiementPersonnelRepository.saveAndFlush(paiementPersonnel);
        paiementPersonnelSearchRepository.save(paiementPersonnel);
        int databaseSizeBeforeDelete = paiementPersonnelRepository.findAll().size();

        // Get the paiementPersonnel
        restPaiementPersonnelMockMvc.perform(delete("/api/paiement-personnels/{id}", paiementPersonnel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean paiementPersonnelExistsInEs = paiementPersonnelSearchRepository.exists(paiementPersonnel.getId());
        assertThat(paiementPersonnelExistsInEs).isFalse();

        // Validate the database is empty
        List<PaiementPersonnel> paiementPersonnels = paiementPersonnelRepository.findAll();
        assertThat(paiementPersonnels).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPaiementPersonnel() throws Exception {
        // Initialize the database
        paiementPersonnelRepository.saveAndFlush(paiementPersonnel);
        paiementPersonnelSearchRepository.save(paiementPersonnel);

        // Search the paiementPersonnel
        restPaiementPersonnelMockMvc.perform(get("/api/_search/paiement-personnels?query=id:" + paiementPersonnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementPersonnel.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].dette").value(hasItem(DEFAULT_DETTE.intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }
}
