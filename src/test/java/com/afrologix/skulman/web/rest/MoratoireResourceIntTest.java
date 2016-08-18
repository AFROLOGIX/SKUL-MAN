package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Moratoire;
import com.afrologix.skulman.repository.MoratoireRepository;
import com.afrologix.skulman.repository.search.MoratoireSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MoratoireResource REST controller.
 *
 * @see MoratoireResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class MoratoireResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_MOTIF = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_NOUVELLE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NOUVELLE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_TYPE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
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
    private MoratoireRepository moratoireRepository;

    @Inject
    private MoratoireSearchRepository moratoireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMoratoireMockMvc;

    private Moratoire moratoire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MoratoireResource moratoireResource = new MoratoireResource();
        ReflectionTestUtils.setField(moratoireResource, "moratoireSearchRepository", moratoireSearchRepository);
        ReflectionTestUtils.setField(moratoireResource, "moratoireRepository", moratoireRepository);
        this.restMoratoireMockMvc = MockMvcBuilders.standaloneSetup(moratoireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        moratoireSearchRepository.deleteAll();
        moratoire = new Moratoire();
        moratoire.setMotif(DEFAULT_MOTIF);
        moratoire.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        moratoire.setNouvelleDate(DEFAULT_NOUVELLE_DATE);
        moratoire.setType(DEFAULT_TYPE);
        moratoire.setCreateBy(DEFAULT_CREATE_BY);
        moratoire.setUpdateBy(DEFAULT_UPDATE_BY);
        moratoire.setCreateAt(DEFAULT_CREATE_AT);
        moratoire.setUpdateAt(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createMoratoire() throws Exception {
        int databaseSizeBeforeCreate = moratoireRepository.findAll().size();

        // Create the Moratoire

        restMoratoireMockMvc.perform(post("/api/moratoires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(moratoire)))
                .andExpect(status().isCreated());

        // Validate the Moratoire in the database
        List<Moratoire> moratoires = moratoireRepository.findAll();
        assertThat(moratoires).hasSize(databaseSizeBeforeCreate + 1);
        Moratoire testMoratoire = moratoires.get(moratoires.size() - 1);
        assertThat(testMoratoire.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testMoratoire.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testMoratoire.getNouvelleDate()).isEqualTo(DEFAULT_NOUVELLE_DATE);
        assertThat(testMoratoire.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMoratoire.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testMoratoire.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testMoratoire.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testMoratoire.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the Moratoire in ElasticSearch
        Moratoire moratoireEs = moratoireSearchRepository.findOne(testMoratoire.getId());
        assertThat(moratoireEs).isEqualToComparingFieldByField(testMoratoire);
    }

    @Test
    @Transactional
    public void getAllMoratoires() throws Exception {
        // Initialize the database
        moratoireRepository.saveAndFlush(moratoire);

        // Get all the moratoires
        restMoratoireMockMvc.perform(get("/api/moratoires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(moratoire.getId().intValue())))
                .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].nouvelleDate").value(hasItem(DEFAULT_NOUVELLE_DATE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
                .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }

    @Test
    @Transactional
    public void getMoratoire() throws Exception {
        // Initialize the database
        moratoireRepository.saveAndFlush(moratoire);

        // Get the moratoire
        restMoratoireMockMvc.perform(get("/api/moratoires/{id}", moratoire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(moratoire.getId().intValue()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF.toString()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.nouvelleDate").value(DEFAULT_NOUVELLE_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT_STR))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingMoratoire() throws Exception {
        // Get the moratoire
        restMoratoireMockMvc.perform(get("/api/moratoires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMoratoire() throws Exception {
        // Initialize the database
        moratoireRepository.saveAndFlush(moratoire);
        moratoireSearchRepository.save(moratoire);
        int databaseSizeBeforeUpdate = moratoireRepository.findAll().size();

        // Update the moratoire
        Moratoire updatedMoratoire = new Moratoire();
        updatedMoratoire.setId(moratoire.getId());
        updatedMoratoire.setMotif(UPDATED_MOTIF);
        updatedMoratoire.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedMoratoire.setNouvelleDate(UPDATED_NOUVELLE_DATE);
        updatedMoratoire.setType(UPDATED_TYPE);
        updatedMoratoire.setCreateBy(UPDATED_CREATE_BY);
        updatedMoratoire.setUpdateBy(UPDATED_UPDATE_BY);
        updatedMoratoire.setCreateAt(UPDATED_CREATE_AT);
        updatedMoratoire.setUpdateAt(UPDATED_UPDATE_AT);

        restMoratoireMockMvc.perform(put("/api/moratoires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMoratoire)))
                .andExpect(status().isOk());

        // Validate the Moratoire in the database
        List<Moratoire> moratoires = moratoireRepository.findAll();
        assertThat(moratoires).hasSize(databaseSizeBeforeUpdate);
        Moratoire testMoratoire = moratoires.get(moratoires.size() - 1);
        assertThat(testMoratoire.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testMoratoire.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testMoratoire.getNouvelleDate()).isEqualTo(UPDATED_NOUVELLE_DATE);
        assertThat(testMoratoire.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMoratoire.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testMoratoire.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testMoratoire.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testMoratoire.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the Moratoire in ElasticSearch
        Moratoire moratoireEs = moratoireSearchRepository.findOne(testMoratoire.getId());
        assertThat(moratoireEs).isEqualToComparingFieldByField(testMoratoire);
    }

    @Test
    @Transactional
    public void deleteMoratoire() throws Exception {
        // Initialize the database
        moratoireRepository.saveAndFlush(moratoire);
        moratoireSearchRepository.save(moratoire);
        int databaseSizeBeforeDelete = moratoireRepository.findAll().size();

        // Get the moratoire
        restMoratoireMockMvc.perform(delete("/api/moratoires/{id}", moratoire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean moratoireExistsInEs = moratoireSearchRepository.exists(moratoire.getId());
        assertThat(moratoireExistsInEs).isFalse();

        // Validate the database is empty
        List<Moratoire> moratoires = moratoireRepository.findAll();
        assertThat(moratoires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMoratoire() throws Exception {
        // Initialize the database
        moratoireRepository.saveAndFlush(moratoire);
        moratoireSearchRepository.save(moratoire);

        // Search the moratoire
        restMoratoireMockMvc.perform(get("/api/_search/moratoires?query=id:" + moratoire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moratoire.getId().intValue())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].nouvelleDate").value(hasItem(DEFAULT_NOUVELLE_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }
}
