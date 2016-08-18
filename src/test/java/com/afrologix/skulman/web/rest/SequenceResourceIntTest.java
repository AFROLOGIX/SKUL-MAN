package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Sequence;
import com.afrologix.skulman.repository.SequenceRepository;
import com.afrologix.skulman.repository.search.SequenceSearchRepository;

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
 * Test class for the SequenceResource REST controller.
 *
 * @see SequenceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class SequenceResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Inject
    private SequenceRepository sequenceRepository;

    @Inject
    private SequenceSearchRepository sequenceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSequenceMockMvc;

    private Sequence sequence;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SequenceResource sequenceResource = new SequenceResource();
        ReflectionTestUtils.setField(sequenceResource, "sequenceSearchRepository", sequenceSearchRepository);
        ReflectionTestUtils.setField(sequenceResource, "sequenceRepository", sequenceRepository);
        this.restSequenceMockMvc = MockMvcBuilders.standaloneSetup(sequenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sequenceSearchRepository.deleteAll();
        sequence = new Sequence();
        sequence.setCode(DEFAULT_CODE);
        sequence.setLibelleFr(DEFAULT_LIBELLE_FR);
        sequence.setLibelleEn(DEFAULT_LIBELLE_EN);
        sequence.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        sequence.setDateDeb(DEFAULT_DATE_DEB);
        sequence.setDateFin(DEFAULT_DATE_FIN);
        sequence.setIsActive(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createSequence() throws Exception {
        int databaseSizeBeforeCreate = sequenceRepository.findAll().size();

        // Create the Sequence

        restSequenceMockMvc.perform(post("/api/sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sequence)))
                .andExpect(status().isCreated());

        // Validate the Sequence in the database
        List<Sequence> sequences = sequenceRepository.findAll();
        assertThat(sequences).hasSize(databaseSizeBeforeCreate + 1);
        Sequence testSequence = sequences.get(sequences.size() - 1);
        assertThat(testSequence.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSequence.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testSequence.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testSequence.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testSequence.getDateDeb()).isEqualTo(DEFAULT_DATE_DEB);
        assertThat(testSequence.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testSequence.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the Sequence in ElasticSearch
        Sequence sequenceEs = sequenceSearchRepository.findOne(testSequence.getId());
        assertThat(sequenceEs).isEqualToComparingFieldByField(testSequence);
    }

    @Test
    @Transactional
    public void getAllSequences() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequences
        restSequenceMockMvc.perform(get("/api/sequences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sequence.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get the sequence
        restSequenceMockMvc.perform(get("/api/sequences/{id}", sequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sequence.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.dateDeb").value(DEFAULT_DATE_DEB.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSequence() throws Exception {
        // Get the sequence
        restSequenceMockMvc.perform(get("/api/sequences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);
        sequenceSearchRepository.save(sequence);
        int databaseSizeBeforeUpdate = sequenceRepository.findAll().size();

        // Update the sequence
        Sequence updatedSequence = new Sequence();
        updatedSequence.setId(sequence.getId());
        updatedSequence.setCode(UPDATED_CODE);
        updatedSequence.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedSequence.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedSequence.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedSequence.setDateDeb(UPDATED_DATE_DEB);
        updatedSequence.setDateFin(UPDATED_DATE_FIN);
        updatedSequence.setIsActive(UPDATED_IS_ACTIVE);

        restSequenceMockMvc.perform(put("/api/sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSequence)))
                .andExpect(status().isOk());

        // Validate the Sequence in the database
        List<Sequence> sequences = sequenceRepository.findAll();
        assertThat(sequences).hasSize(databaseSizeBeforeUpdate);
        Sequence testSequence = sequences.get(sequences.size() - 1);
        assertThat(testSequence.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSequence.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testSequence.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testSequence.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testSequence.getDateDeb()).isEqualTo(UPDATED_DATE_DEB);
        assertThat(testSequence.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testSequence.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the Sequence in ElasticSearch
        Sequence sequenceEs = sequenceSearchRepository.findOne(testSequence.getId());
        assertThat(sequenceEs).isEqualToComparingFieldByField(testSequence);
    }

    @Test
    @Transactional
    public void deleteSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);
        sequenceSearchRepository.save(sequence);
        int databaseSizeBeforeDelete = sequenceRepository.findAll().size();

        // Get the sequence
        restSequenceMockMvc.perform(delete("/api/sequences/{id}", sequence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean sequenceExistsInEs = sequenceSearchRepository.exists(sequence.getId());
        assertThat(sequenceExistsInEs).isFalse();

        // Validate the database is empty
        List<Sequence> sequences = sequenceRepository.findAll();
        assertThat(sequences).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);
        sequenceSearchRepository.save(sequence);

        // Search the sequence
        restSequenceMockMvc.perform(get("/api/_search/sequences?query=id:" + sequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
