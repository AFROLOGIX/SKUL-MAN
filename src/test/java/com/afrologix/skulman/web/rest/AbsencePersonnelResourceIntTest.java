package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.AbsencePersonnel;
import com.afrologix.skulman.repository.AbsencePersonnelRepository;
import com.afrologix.skulman.repository.search.AbsencePersonnelSearchRepository;

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
 * Test class for the AbsencePersonnelResource REST controller.
 *
 * @see AbsencePersonnelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class AbsencePersonnelResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PERIODE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PERIODE = "BBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_JUSTIFIEE = false;
    private static final Boolean UPDATED_JUSTIFIEE = true;
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
    private AbsencePersonnelRepository absencePersonnelRepository;

    @Inject
    private AbsencePersonnelSearchRepository absencePersonnelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAbsencePersonnelMockMvc;

    private AbsencePersonnel absencePersonnel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AbsencePersonnelResource absencePersonnelResource = new AbsencePersonnelResource();
        ReflectionTestUtils.setField(absencePersonnelResource, "absencePersonnelSearchRepository", absencePersonnelSearchRepository);
        ReflectionTestUtils.setField(absencePersonnelResource, "absencePersonnelRepository", absencePersonnelRepository);
        this.restAbsencePersonnelMockMvc = MockMvcBuilders.standaloneSetup(absencePersonnelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        absencePersonnelSearchRepository.deleteAll();
        absencePersonnel = new AbsencePersonnel();
        absencePersonnel.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        absencePersonnel.setPeriode(DEFAULT_PERIODE);
        absencePersonnel.setJustifiee(DEFAULT_JUSTIFIEE);
        absencePersonnel.setCreateBy(DEFAULT_CREATE_BY);
        absencePersonnel.setUpdateBy(DEFAULT_UPDATE_BY);
        absencePersonnel.setCreateAt(DEFAULT_CREATE_AT);
        absencePersonnel.setUpdateAt(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createAbsencePersonnel() throws Exception {
        int databaseSizeBeforeCreate = absencePersonnelRepository.findAll().size();

        // Create the AbsencePersonnel

        restAbsencePersonnelMockMvc.perform(post("/api/absence-personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(absencePersonnel)))
                .andExpect(status().isCreated());

        // Validate the AbsencePersonnel in the database
        List<AbsencePersonnel> absencePersonnels = absencePersonnelRepository.findAll();
        assertThat(absencePersonnels).hasSize(databaseSizeBeforeCreate + 1);
        AbsencePersonnel testAbsencePersonnel = absencePersonnels.get(absencePersonnels.size() - 1);
        assertThat(testAbsencePersonnel.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testAbsencePersonnel.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testAbsencePersonnel.isJustifiee()).isEqualTo(DEFAULT_JUSTIFIEE);
        assertThat(testAbsencePersonnel.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testAbsencePersonnel.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testAbsencePersonnel.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testAbsencePersonnel.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the AbsencePersonnel in ElasticSearch
        AbsencePersonnel absencePersonnelEs = absencePersonnelSearchRepository.findOne(testAbsencePersonnel.getId());
        assertThat(absencePersonnelEs).isEqualToComparingFieldByField(testAbsencePersonnel);
    }

    @Test
    @Transactional
    public void checkAnneeScolaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = absencePersonnelRepository.findAll().size();
        // set the field null
        absencePersonnel.setAnneeScolaire(null);

        // Create the AbsencePersonnel, which fails.

        restAbsencePersonnelMockMvc.perform(post("/api/absence-personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(absencePersonnel)))
                .andExpect(status().isBadRequest());

        List<AbsencePersonnel> absencePersonnels = absencePersonnelRepository.findAll();
        assertThat(absencePersonnels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAbsencePersonnels() throws Exception {
        // Initialize the database
        absencePersonnelRepository.saveAndFlush(absencePersonnel);

        // Get all the absencePersonnels
        restAbsencePersonnelMockMvc.perform(get("/api/absence-personnels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(absencePersonnel.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
                .andExpect(jsonPath("$.[*].justifiee").value(hasItem(DEFAULT_JUSTIFIEE.booleanValue())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
                .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }

    @Test
    @Transactional
    public void getAbsencePersonnel() throws Exception {
        // Initialize the database
        absencePersonnelRepository.saveAndFlush(absencePersonnel);

        // Get the absencePersonnel
        restAbsencePersonnelMockMvc.perform(get("/api/absence-personnels/{id}", absencePersonnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(absencePersonnel.getId().intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE.toString()))
            .andExpect(jsonPath("$.justifiee").value(DEFAULT_JUSTIFIEE.booleanValue()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT_STR))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingAbsencePersonnel() throws Exception {
        // Get the absencePersonnel
        restAbsencePersonnelMockMvc.perform(get("/api/absence-personnels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbsencePersonnel() throws Exception {
        // Initialize the database
        absencePersonnelRepository.saveAndFlush(absencePersonnel);
        absencePersonnelSearchRepository.save(absencePersonnel);
        int databaseSizeBeforeUpdate = absencePersonnelRepository.findAll().size();

        // Update the absencePersonnel
        AbsencePersonnel updatedAbsencePersonnel = new AbsencePersonnel();
        updatedAbsencePersonnel.setId(absencePersonnel.getId());
        updatedAbsencePersonnel.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedAbsencePersonnel.setPeriode(UPDATED_PERIODE);
        updatedAbsencePersonnel.setJustifiee(UPDATED_JUSTIFIEE);
        updatedAbsencePersonnel.setCreateBy(UPDATED_CREATE_BY);
        updatedAbsencePersonnel.setUpdateBy(UPDATED_UPDATE_BY);
        updatedAbsencePersonnel.setCreateAt(UPDATED_CREATE_AT);
        updatedAbsencePersonnel.setUpdateAt(UPDATED_UPDATE_AT);

        restAbsencePersonnelMockMvc.perform(put("/api/absence-personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAbsencePersonnel)))
                .andExpect(status().isOk());

        // Validate the AbsencePersonnel in the database
        List<AbsencePersonnel> absencePersonnels = absencePersonnelRepository.findAll();
        assertThat(absencePersonnels).hasSize(databaseSizeBeforeUpdate);
        AbsencePersonnel testAbsencePersonnel = absencePersonnels.get(absencePersonnels.size() - 1);
        assertThat(testAbsencePersonnel.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testAbsencePersonnel.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testAbsencePersonnel.isJustifiee()).isEqualTo(UPDATED_JUSTIFIEE);
        assertThat(testAbsencePersonnel.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testAbsencePersonnel.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testAbsencePersonnel.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testAbsencePersonnel.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the AbsencePersonnel in ElasticSearch
        AbsencePersonnel absencePersonnelEs = absencePersonnelSearchRepository.findOne(testAbsencePersonnel.getId());
        assertThat(absencePersonnelEs).isEqualToComparingFieldByField(testAbsencePersonnel);
    }

    @Test
    @Transactional
    public void deleteAbsencePersonnel() throws Exception {
        // Initialize the database
        absencePersonnelRepository.saveAndFlush(absencePersonnel);
        absencePersonnelSearchRepository.save(absencePersonnel);
        int databaseSizeBeforeDelete = absencePersonnelRepository.findAll().size();

        // Get the absencePersonnel
        restAbsencePersonnelMockMvc.perform(delete("/api/absence-personnels/{id}", absencePersonnel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean absencePersonnelExistsInEs = absencePersonnelSearchRepository.exists(absencePersonnel.getId());
        assertThat(absencePersonnelExistsInEs).isFalse();

        // Validate the database is empty
        List<AbsencePersonnel> absencePersonnels = absencePersonnelRepository.findAll();
        assertThat(absencePersonnels).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAbsencePersonnel() throws Exception {
        // Initialize the database
        absencePersonnelRepository.saveAndFlush(absencePersonnel);
        absencePersonnelSearchRepository.save(absencePersonnel);

        // Search the absencePersonnel
        restAbsencePersonnelMockMvc.perform(get("/api/_search/absence-personnels?query=id:" + absencePersonnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(absencePersonnel.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
            .andExpect(jsonPath("$.[*].justifiee").value(hasItem(DEFAULT_JUSTIFIEE.booleanValue())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }
}
