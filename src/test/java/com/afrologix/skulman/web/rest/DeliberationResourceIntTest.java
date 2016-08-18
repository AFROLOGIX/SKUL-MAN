package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Deliberation;
import com.afrologix.skulman.repository.DeliberationRepository;
import com.afrologix.skulman.repository.search.DeliberationSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DeliberationResource REST controller.
 *
 * @see DeliberationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class DeliberationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_MOTIF = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_DECISION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DECISION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
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
    private DeliberationRepository deliberationRepository;

    @Inject
    private DeliberationSearchRepository deliberationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDeliberationMockMvc;

    private Deliberation deliberation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeliberationResource deliberationResource = new DeliberationResource();
        ReflectionTestUtils.setField(deliberationResource, "deliberationSearchRepository", deliberationSearchRepository);
        ReflectionTestUtils.setField(deliberationResource, "deliberationRepository", deliberationRepository);
        this.restDeliberationMockMvc = MockMvcBuilders.standaloneSetup(deliberationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        deliberationSearchRepository.deleteAll();
        deliberation = new Deliberation();
        deliberation.setMotif(DEFAULT_MOTIF);
        deliberation.setDecision(DEFAULT_DECISION);
        deliberation.setIsActive(DEFAULT_IS_ACTIVE);
        deliberation.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        deliberation.setCreateBy(DEFAULT_CREATE_BY);
        deliberation.setUpdateBy(DEFAULT_UPDATE_BY);
        deliberation.setCreateAt(DEFAULT_CREATE_AT);
        deliberation.setUpdateAt(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createDeliberation() throws Exception {
        int databaseSizeBeforeCreate = deliberationRepository.findAll().size();

        // Create the Deliberation

        restDeliberationMockMvc.perform(post("/api/deliberations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliberation)))
                .andExpect(status().isCreated());

        // Validate the Deliberation in the database
        List<Deliberation> deliberations = deliberationRepository.findAll();
        assertThat(deliberations).hasSize(databaseSizeBeforeCreate + 1);
        Deliberation testDeliberation = deliberations.get(deliberations.size() - 1);
        assertThat(testDeliberation.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testDeliberation.getDecision()).isEqualTo(DEFAULT_DECISION);
        assertThat(testDeliberation.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testDeliberation.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testDeliberation.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testDeliberation.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testDeliberation.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testDeliberation.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the Deliberation in ElasticSearch
        Deliberation deliberationEs = deliberationSearchRepository.findOne(testDeliberation.getId());
        assertThat(deliberationEs).isEqualToComparingFieldByField(testDeliberation);
    }

    @Test
    @Transactional
    public void getAllDeliberations() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        // Get all the deliberations
        restDeliberationMockMvc.perform(get("/api/deliberations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(deliberation.getId().intValue())))
                .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
                .andExpect(jsonPath("$.[*].decision").value(hasItem(DEFAULT_DECISION.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
                .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }

    @Test
    @Transactional
    public void getDeliberation() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        // Get the deliberation
        restDeliberationMockMvc.perform(get("/api/deliberations/{id}", deliberation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(deliberation.getId().intValue()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF.toString()))
            .andExpect(jsonPath("$.decision").value(DEFAULT_DECISION.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT_STR))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingDeliberation() throws Exception {
        // Get the deliberation
        restDeliberationMockMvc.perform(get("/api/deliberations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeliberation() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);
        deliberationSearchRepository.save(deliberation);
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();

        // Update the deliberation
        Deliberation updatedDeliberation = new Deliberation();
        updatedDeliberation.setId(deliberation.getId());
        updatedDeliberation.setMotif(UPDATED_MOTIF);
        updatedDeliberation.setDecision(UPDATED_DECISION);
        updatedDeliberation.setIsActive(UPDATED_IS_ACTIVE);
        updatedDeliberation.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedDeliberation.setCreateBy(UPDATED_CREATE_BY);
        updatedDeliberation.setUpdateBy(UPDATED_UPDATE_BY);
        updatedDeliberation.setCreateAt(UPDATED_CREATE_AT);
        updatedDeliberation.setUpdateAt(UPDATED_UPDATE_AT);

        restDeliberationMockMvc.perform(put("/api/deliberations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDeliberation)))
                .andExpect(status().isOk());

        // Validate the Deliberation in the database
        List<Deliberation> deliberations = deliberationRepository.findAll();
        assertThat(deliberations).hasSize(databaseSizeBeforeUpdate);
        Deliberation testDeliberation = deliberations.get(deliberations.size() - 1);
        assertThat(testDeliberation.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testDeliberation.getDecision()).isEqualTo(UPDATED_DECISION);
        assertThat(testDeliberation.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testDeliberation.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testDeliberation.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testDeliberation.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testDeliberation.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testDeliberation.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the Deliberation in ElasticSearch
        Deliberation deliberationEs = deliberationSearchRepository.findOne(testDeliberation.getId());
        assertThat(deliberationEs).isEqualToComparingFieldByField(testDeliberation);
    }

    @Test
    @Transactional
    public void deleteDeliberation() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);
        deliberationSearchRepository.save(deliberation);
        int databaseSizeBeforeDelete = deliberationRepository.findAll().size();

        // Get the deliberation
        restDeliberationMockMvc.perform(delete("/api/deliberations/{id}", deliberation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean deliberationExistsInEs = deliberationSearchRepository.exists(deliberation.getId());
        assertThat(deliberationExistsInEs).isFalse();

        // Validate the database is empty
        List<Deliberation> deliberations = deliberationRepository.findAll();
        assertThat(deliberations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDeliberation() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);
        deliberationSearchRepository.save(deliberation);

        // Search the deliberation
        restDeliberationMockMvc.perform(get("/api/_search/deliberations?query=id:" + deliberation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliberation.getId().intValue())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
            .andExpect(jsonPath("$.[*].decision").value(hasItem(DEFAULT_DECISION.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }
}
