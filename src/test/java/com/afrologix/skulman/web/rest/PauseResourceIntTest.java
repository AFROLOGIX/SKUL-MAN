package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Pause;
import com.afrologix.skulman.repository.PauseRepository;
import com.afrologix.skulman.repository.search.PauseSearchRepository;

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
 * Test class for the PauseResource REST controller.
 *
 * @see PauseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class PauseResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_HEURE_DEB = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HEURE_DEB = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HEURE_DEB_STR = dateTimeFormatter.format(DEFAULT_HEURE_DEB);

    private static final ZonedDateTime DEFAULT_HEURE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HEURE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HEURE_FIN_STR = dateTimeFormatter.format(DEFAULT_HEURE_FIN);

    @Inject
    private PauseRepository pauseRepository;

    @Inject
    private PauseSearchRepository pauseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPauseMockMvc;

    private Pause pause;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PauseResource pauseResource = new PauseResource();
        ReflectionTestUtils.setField(pauseResource, "pauseSearchRepository", pauseSearchRepository);
        ReflectionTestUtils.setField(pauseResource, "pauseRepository", pauseRepository);
        this.restPauseMockMvc = MockMvcBuilders.standaloneSetup(pauseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pauseSearchRepository.deleteAll();
        pause = new Pause();
        pause.setCode(DEFAULT_CODE);
        pause.setLibelleFr(DEFAULT_LIBELLE_FR);
        pause.setLibelleEn(DEFAULT_LIBELLE_EN);
        pause.setHeureDeb(DEFAULT_HEURE_DEB);
        pause.setHeureFin(DEFAULT_HEURE_FIN);
    }

    @Test
    @Transactional
    public void createPause() throws Exception {
        int databaseSizeBeforeCreate = pauseRepository.findAll().size();

        // Create the Pause

        restPauseMockMvc.perform(post("/api/pauses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pause)))
                .andExpect(status().isCreated());

        // Validate the Pause in the database
        List<Pause> pauses = pauseRepository.findAll();
        assertThat(pauses).hasSize(databaseSizeBeforeCreate + 1);
        Pause testPause = pauses.get(pauses.size() - 1);
        assertThat(testPause.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPause.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testPause.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testPause.getHeureDeb()).isEqualTo(DEFAULT_HEURE_DEB);
        assertThat(testPause.getHeureFin()).isEqualTo(DEFAULT_HEURE_FIN);

        // Validate the Pause in ElasticSearch
        Pause pauseEs = pauseSearchRepository.findOne(testPause.getId());
        assertThat(pauseEs).isEqualToComparingFieldByField(testPause);
    }

    @Test
    @Transactional
    public void getAllPauses() throws Exception {
        // Initialize the database
        pauseRepository.saveAndFlush(pause);

        // Get all the pauses
        restPauseMockMvc.perform(get("/api/pauses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pause.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].heureDeb").value(hasItem(DEFAULT_HEURE_DEB_STR)))
                .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN_STR)));
    }

    @Test
    @Transactional
    public void getPause() throws Exception {
        // Initialize the database
        pauseRepository.saveAndFlush(pause);

        // Get the pause
        restPauseMockMvc.perform(get("/api/pauses/{id}", pause.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pause.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.heureDeb").value(DEFAULT_HEURE_DEB_STR))
            .andExpect(jsonPath("$.heureFin").value(DEFAULT_HEURE_FIN_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPause() throws Exception {
        // Get the pause
        restPauseMockMvc.perform(get("/api/pauses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePause() throws Exception {
        // Initialize the database
        pauseRepository.saveAndFlush(pause);
        pauseSearchRepository.save(pause);
        int databaseSizeBeforeUpdate = pauseRepository.findAll().size();

        // Update the pause
        Pause updatedPause = new Pause();
        updatedPause.setId(pause.getId());
        updatedPause.setCode(UPDATED_CODE);
        updatedPause.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedPause.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedPause.setHeureDeb(UPDATED_HEURE_DEB);
        updatedPause.setHeureFin(UPDATED_HEURE_FIN);

        restPauseMockMvc.perform(put("/api/pauses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPause)))
                .andExpect(status().isOk());

        // Validate the Pause in the database
        List<Pause> pauses = pauseRepository.findAll();
        assertThat(pauses).hasSize(databaseSizeBeforeUpdate);
        Pause testPause = pauses.get(pauses.size() - 1);
        assertThat(testPause.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPause.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testPause.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testPause.getHeureDeb()).isEqualTo(UPDATED_HEURE_DEB);
        assertThat(testPause.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);

        // Validate the Pause in ElasticSearch
        Pause pauseEs = pauseSearchRepository.findOne(testPause.getId());
        assertThat(pauseEs).isEqualToComparingFieldByField(testPause);
    }

    @Test
    @Transactional
    public void deletePause() throws Exception {
        // Initialize the database
        pauseRepository.saveAndFlush(pause);
        pauseSearchRepository.save(pause);
        int databaseSizeBeforeDelete = pauseRepository.findAll().size();

        // Get the pause
        restPauseMockMvc.perform(delete("/api/pauses/{id}", pause.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean pauseExistsInEs = pauseSearchRepository.exists(pause.getId());
        assertThat(pauseExistsInEs).isFalse();

        // Validate the database is empty
        List<Pause> pauses = pauseRepository.findAll();
        assertThat(pauses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPause() throws Exception {
        // Initialize the database
        pauseRepository.saveAndFlush(pause);
        pauseSearchRepository.save(pause);

        // Search the pause
        restPauseMockMvc.perform(get("/api/_search/pauses?query=id:" + pause.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pause.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].heureDeb").value(hasItem(DEFAULT_HEURE_DEB_STR)))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN_STR)));
    }
}
