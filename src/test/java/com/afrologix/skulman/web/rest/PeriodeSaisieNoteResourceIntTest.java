package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.PeriodeSaisieNote;
import com.afrologix.skulman.repository.PeriodeSaisieNoteRepository;
import com.afrologix.skulman.repository.search.PeriodeSaisieNoteSearchRepository;

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
 * Test class for the PeriodeSaisieNoteResource REST controller.
 *
 * @see PeriodeSaisieNoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class PeriodeSaisieNoteResourceIntTest {

    private static final String DEFAULT_TYPE_PERIODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TYPE_PERIODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CLOSE = false;
    private static final Boolean UPDATED_IS_CLOSE = true;

    @Inject
    private PeriodeSaisieNoteRepository periodeSaisieNoteRepository;

    @Inject
    private PeriodeSaisieNoteSearchRepository periodeSaisieNoteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPeriodeSaisieNoteMockMvc;

    private PeriodeSaisieNote periodeSaisieNote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PeriodeSaisieNoteResource periodeSaisieNoteResource = new PeriodeSaisieNoteResource();
        ReflectionTestUtils.setField(periodeSaisieNoteResource, "periodeSaisieNoteSearchRepository", periodeSaisieNoteSearchRepository);
        ReflectionTestUtils.setField(periodeSaisieNoteResource, "periodeSaisieNoteRepository", periodeSaisieNoteRepository);
        this.restPeriodeSaisieNoteMockMvc = MockMvcBuilders.standaloneSetup(periodeSaisieNoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        periodeSaisieNoteSearchRepository.deleteAll();
        periodeSaisieNote = new PeriodeSaisieNote();
        periodeSaisieNote.setTypePeriode(DEFAULT_TYPE_PERIODE);
        periodeSaisieNote.setDateDeb(DEFAULT_DATE_DEB);
        periodeSaisieNote.setDateFin(DEFAULT_DATE_FIN);
        periodeSaisieNote.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        periodeSaisieNote.setIsClose(DEFAULT_IS_CLOSE);
    }

    @Test
    @Transactional
    public void createPeriodeSaisieNote() throws Exception {
        int databaseSizeBeforeCreate = periodeSaisieNoteRepository.findAll().size();

        // Create the PeriodeSaisieNote

        restPeriodeSaisieNoteMockMvc.perform(post("/api/periode-saisie-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(periodeSaisieNote)))
                .andExpect(status().isCreated());

        // Validate the PeriodeSaisieNote in the database
        List<PeriodeSaisieNote> periodeSaisieNotes = periodeSaisieNoteRepository.findAll();
        assertThat(periodeSaisieNotes).hasSize(databaseSizeBeforeCreate + 1);
        PeriodeSaisieNote testPeriodeSaisieNote = periodeSaisieNotes.get(periodeSaisieNotes.size() - 1);
        assertThat(testPeriodeSaisieNote.getTypePeriode()).isEqualTo(DEFAULT_TYPE_PERIODE);
        assertThat(testPeriodeSaisieNote.getDateDeb()).isEqualTo(DEFAULT_DATE_DEB);
        assertThat(testPeriodeSaisieNote.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testPeriodeSaisieNote.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testPeriodeSaisieNote.isIsClose()).isEqualTo(DEFAULT_IS_CLOSE);

        // Validate the PeriodeSaisieNote in ElasticSearch
        PeriodeSaisieNote periodeSaisieNoteEs = periodeSaisieNoteSearchRepository.findOne(testPeriodeSaisieNote.getId());
        assertThat(periodeSaisieNoteEs).isEqualToComparingFieldByField(testPeriodeSaisieNote);
    }

    @Test
    @Transactional
    public void getAllPeriodeSaisieNotes() throws Exception {
        // Initialize the database
        periodeSaisieNoteRepository.saveAndFlush(periodeSaisieNote);

        // Get all the periodeSaisieNotes
        restPeriodeSaisieNoteMockMvc.perform(get("/api/periode-saisie-notes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(periodeSaisieNote.getId().intValue())))
                .andExpect(jsonPath("$.[*].typePeriode").value(hasItem(DEFAULT_TYPE_PERIODE.toString())))
                .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].isClose").value(hasItem(DEFAULT_IS_CLOSE.booleanValue())));
    }

    @Test
    @Transactional
    public void getPeriodeSaisieNote() throws Exception {
        // Initialize the database
        periodeSaisieNoteRepository.saveAndFlush(periodeSaisieNote);

        // Get the periodeSaisieNote
        restPeriodeSaisieNoteMockMvc.perform(get("/api/periode-saisie-notes/{id}", periodeSaisieNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(periodeSaisieNote.getId().intValue()))
            .andExpect(jsonPath("$.typePeriode").value(DEFAULT_TYPE_PERIODE.toString()))
            .andExpect(jsonPath("$.dateDeb").value(DEFAULT_DATE_DEB.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.isClose").value(DEFAULT_IS_CLOSE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPeriodeSaisieNote() throws Exception {
        // Get the periodeSaisieNote
        restPeriodeSaisieNoteMockMvc.perform(get("/api/periode-saisie-notes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriodeSaisieNote() throws Exception {
        // Initialize the database
        periodeSaisieNoteRepository.saveAndFlush(periodeSaisieNote);
        periodeSaisieNoteSearchRepository.save(periodeSaisieNote);
        int databaseSizeBeforeUpdate = periodeSaisieNoteRepository.findAll().size();

        // Update the periodeSaisieNote
        PeriodeSaisieNote updatedPeriodeSaisieNote = new PeriodeSaisieNote();
        updatedPeriodeSaisieNote.setId(periodeSaisieNote.getId());
        updatedPeriodeSaisieNote.setTypePeriode(UPDATED_TYPE_PERIODE);
        updatedPeriodeSaisieNote.setDateDeb(UPDATED_DATE_DEB);
        updatedPeriodeSaisieNote.setDateFin(UPDATED_DATE_FIN);
        updatedPeriodeSaisieNote.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedPeriodeSaisieNote.setIsClose(UPDATED_IS_CLOSE);

        restPeriodeSaisieNoteMockMvc.perform(put("/api/periode-saisie-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPeriodeSaisieNote)))
                .andExpect(status().isOk());

        // Validate the PeriodeSaisieNote in the database
        List<PeriodeSaisieNote> periodeSaisieNotes = periodeSaisieNoteRepository.findAll();
        assertThat(periodeSaisieNotes).hasSize(databaseSizeBeforeUpdate);
        PeriodeSaisieNote testPeriodeSaisieNote = periodeSaisieNotes.get(periodeSaisieNotes.size() - 1);
        assertThat(testPeriodeSaisieNote.getTypePeriode()).isEqualTo(UPDATED_TYPE_PERIODE);
        assertThat(testPeriodeSaisieNote.getDateDeb()).isEqualTo(UPDATED_DATE_DEB);
        assertThat(testPeriodeSaisieNote.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testPeriodeSaisieNote.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testPeriodeSaisieNote.isIsClose()).isEqualTo(UPDATED_IS_CLOSE);

        // Validate the PeriodeSaisieNote in ElasticSearch
        PeriodeSaisieNote periodeSaisieNoteEs = periodeSaisieNoteSearchRepository.findOne(testPeriodeSaisieNote.getId());
        assertThat(periodeSaisieNoteEs).isEqualToComparingFieldByField(testPeriodeSaisieNote);
    }

    @Test
    @Transactional
    public void deletePeriodeSaisieNote() throws Exception {
        // Initialize the database
        periodeSaisieNoteRepository.saveAndFlush(periodeSaisieNote);
        periodeSaisieNoteSearchRepository.save(periodeSaisieNote);
        int databaseSizeBeforeDelete = periodeSaisieNoteRepository.findAll().size();

        // Get the periodeSaisieNote
        restPeriodeSaisieNoteMockMvc.perform(delete("/api/periode-saisie-notes/{id}", periodeSaisieNote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean periodeSaisieNoteExistsInEs = periodeSaisieNoteSearchRepository.exists(periodeSaisieNote.getId());
        assertThat(periodeSaisieNoteExistsInEs).isFalse();

        // Validate the database is empty
        List<PeriodeSaisieNote> periodeSaisieNotes = periodeSaisieNoteRepository.findAll();
        assertThat(periodeSaisieNotes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPeriodeSaisieNote() throws Exception {
        // Initialize the database
        periodeSaisieNoteRepository.saveAndFlush(periodeSaisieNote);
        periodeSaisieNoteSearchRepository.save(periodeSaisieNote);

        // Search the periodeSaisieNote
        restPeriodeSaisieNoteMockMvc.perform(get("/api/_search/periode-saisie-notes?query=id:" + periodeSaisieNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodeSaisieNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].typePeriode").value(hasItem(DEFAULT_TYPE_PERIODE.toString())))
            .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].isClose").value(hasItem(DEFAULT_IS_CLOSE.booleanValue())));
    }
}
