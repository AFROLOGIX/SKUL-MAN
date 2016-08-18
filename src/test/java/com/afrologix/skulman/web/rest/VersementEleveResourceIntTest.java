package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.VersementEleve;
import com.afrologix.skulman.repository.VersementEleveRepository;
import com.afrologix.skulman.repository.search.VersementEleveSearchRepository;

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
 * Test class for the VersementEleveResource REST controller.
 *
 * @see VersementEleveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class VersementEleveResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PERIODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PERIODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DETTE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DETTE = new BigDecimal(2);
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
    private VersementEleveRepository versementEleveRepository;

    @Inject
    private VersementEleveSearchRepository versementEleveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVersementEleveMockMvc;

    private VersementEleve versementEleve;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VersementEleveResource versementEleveResource = new VersementEleveResource();
        ReflectionTestUtils.setField(versementEleveResource, "versementEleveSearchRepository", versementEleveSearchRepository);
        ReflectionTestUtils.setField(versementEleveResource, "versementEleveRepository", versementEleveRepository);
        this.restVersementEleveMockMvc = MockMvcBuilders.standaloneSetup(versementEleveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        versementEleveSearchRepository.deleteAll();
        versementEleve = new VersementEleve();
        versementEleve.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        versementEleve.setPeriode(DEFAULT_PERIODE);
        versementEleve.setMontant(DEFAULT_MONTANT);
        versementEleve.setDette(DEFAULT_DETTE);
        versementEleve.setCreateBy(DEFAULT_CREATE_BY);
        versementEleve.setUpdateBy(DEFAULT_UPDATE_BY);
        versementEleve.setCreateAt(DEFAULT_CREATE_AT);
        versementEleve.setUpdateAt(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createVersementEleve() throws Exception {
        int databaseSizeBeforeCreate = versementEleveRepository.findAll().size();

        // Create the VersementEleve

        restVersementEleveMockMvc.perform(post("/api/versement-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(versementEleve)))
                .andExpect(status().isCreated());

        // Validate the VersementEleve in the database
        List<VersementEleve> versementEleves = versementEleveRepository.findAll();
        assertThat(versementEleves).hasSize(databaseSizeBeforeCreate + 1);
        VersementEleve testVersementEleve = versementEleves.get(versementEleves.size() - 1);
        assertThat(testVersementEleve.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testVersementEleve.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testVersementEleve.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testVersementEleve.getDette()).isEqualTo(DEFAULT_DETTE);
        assertThat(testVersementEleve.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testVersementEleve.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testVersementEleve.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testVersementEleve.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the VersementEleve in ElasticSearch
        VersementEleve versementEleveEs = versementEleveSearchRepository.findOne(testVersementEleve.getId());
        assertThat(versementEleveEs).isEqualToComparingFieldByField(testVersementEleve);
    }

    @Test
    @Transactional
    public void getAllVersementEleves() throws Exception {
        // Initialize the database
        versementEleveRepository.saveAndFlush(versementEleve);

        // Get all the versementEleves
        restVersementEleveMockMvc.perform(get("/api/versement-eleves?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(versementEleve.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
                .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
                .andExpect(jsonPath("$.[*].dette").value(hasItem(DEFAULT_DETTE.intValue())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
                .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }

    @Test
    @Transactional
    public void getVersementEleve() throws Exception {
        // Initialize the database
        versementEleveRepository.saveAndFlush(versementEleve);

        // Get the versementEleve
        restVersementEleveMockMvc.perform(get("/api/versement-eleves/{id}", versementEleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(versementEleve.getId().intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.dette").value(DEFAULT_DETTE.intValue()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT_STR))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingVersementEleve() throws Exception {
        // Get the versementEleve
        restVersementEleveMockMvc.perform(get("/api/versement-eleves/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVersementEleve() throws Exception {
        // Initialize the database
        versementEleveRepository.saveAndFlush(versementEleve);
        versementEleveSearchRepository.save(versementEleve);
        int databaseSizeBeforeUpdate = versementEleveRepository.findAll().size();

        // Update the versementEleve
        VersementEleve updatedVersementEleve = new VersementEleve();
        updatedVersementEleve.setId(versementEleve.getId());
        updatedVersementEleve.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedVersementEleve.setPeriode(UPDATED_PERIODE);
        updatedVersementEleve.setMontant(UPDATED_MONTANT);
        updatedVersementEleve.setDette(UPDATED_DETTE);
        updatedVersementEleve.setCreateBy(UPDATED_CREATE_BY);
        updatedVersementEleve.setUpdateBy(UPDATED_UPDATE_BY);
        updatedVersementEleve.setCreateAt(UPDATED_CREATE_AT);
        updatedVersementEleve.setUpdateAt(UPDATED_UPDATE_AT);

        restVersementEleveMockMvc.perform(put("/api/versement-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVersementEleve)))
                .andExpect(status().isOk());

        // Validate the VersementEleve in the database
        List<VersementEleve> versementEleves = versementEleveRepository.findAll();
        assertThat(versementEleves).hasSize(databaseSizeBeforeUpdate);
        VersementEleve testVersementEleve = versementEleves.get(versementEleves.size() - 1);
        assertThat(testVersementEleve.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testVersementEleve.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testVersementEleve.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testVersementEleve.getDette()).isEqualTo(UPDATED_DETTE);
        assertThat(testVersementEleve.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testVersementEleve.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testVersementEleve.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testVersementEleve.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the VersementEleve in ElasticSearch
        VersementEleve versementEleveEs = versementEleveSearchRepository.findOne(testVersementEleve.getId());
        assertThat(versementEleveEs).isEqualToComparingFieldByField(testVersementEleve);
    }

    @Test
    @Transactional
    public void deleteVersementEleve() throws Exception {
        // Initialize the database
        versementEleveRepository.saveAndFlush(versementEleve);
        versementEleveSearchRepository.save(versementEleve);
        int databaseSizeBeforeDelete = versementEleveRepository.findAll().size();

        // Get the versementEleve
        restVersementEleveMockMvc.perform(delete("/api/versement-eleves/{id}", versementEleve.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean versementEleveExistsInEs = versementEleveSearchRepository.exists(versementEleve.getId());
        assertThat(versementEleveExistsInEs).isFalse();

        // Validate the database is empty
        List<VersementEleve> versementEleves = versementEleveRepository.findAll();
        assertThat(versementEleves).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVersementEleve() throws Exception {
        // Initialize the database
        versementEleveRepository.saveAndFlush(versementEleve);
        versementEleveSearchRepository.save(versementEleve);

        // Search the versementEleve
        restVersementEleveMockMvc.perform(get("/api/_search/versement-eleves?query=id:" + versementEleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(versementEleve.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].dette").value(hasItem(DEFAULT_DETTE.intValue())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }
}
