package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.OptionBulletinNote;
import com.afrologix.skulman.repository.OptionBulletinNoteRepository;
import com.afrologix.skulman.repository.search.OptionBulletinNoteSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the OptionBulletinNoteResource REST controller.
 *
 * @see OptionBulletinNoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class OptionBulletinNoteResourceIntTest {


    private static final Boolean DEFAULT_NOM_ENSEIGNANT = false;
    private static final Boolean UPDATED_NOM_ENSEIGNANT = true;

    private static final Boolean DEFAULT_COEF = false;
    private static final Boolean UPDATED_COEF = true;

    private static final Boolean DEFAULT_NOTE_MIN = false;
    private static final Boolean UPDATED_NOTE_MIN = true;

    private static final Boolean DEFAULT_NOTE_MAX = false;
    private static final Boolean UPDATED_NOTE_MAX = true;

    private static final Boolean DEFAULT_RANG_MATIERE = false;
    private static final Boolean UPDATED_RANG_MATIERE = true;

    private static final Boolean DEFAULT_MOYENNE_MATIERE = false;
    private static final Boolean UPDATED_MOYENNE_MATIERE = true;

    private static final Boolean DEFAULT_APPRECIATION = false;
    private static final Boolean UPDATED_APPRECIATION = true;

    private static final Boolean DEFAULT_MOYENNE_GENERALE_CLASSE = false;
    private static final Boolean UPDATED_MOYENNE_GENERALE_CLASSE = true;

    private static final Boolean DEFAULT_GROUPE_MATIERE = false;
    private static final Boolean UPDATED_GROUPE_MATIERE = true;

    private static final Boolean DEFAULT_PHOTO = false;
    private static final Boolean UPDATED_PHOTO = true;

    private static final Boolean DEFAULT_TOTAL_ELEVE = false;
    private static final Boolean UPDATED_TOTAL_ELEVE = true;

    private static final Boolean DEFAULT_AFFICHER_SANCTION = false;
    private static final Boolean UPDATED_AFFICHER_SANCTION = true;

    private static final Boolean DEFAULT_AFFICHER_MATRICULE = false;
    private static final Boolean UPDATED_AFFICHER_MATRICULE = true;

    @Inject
    private OptionBulletinNoteRepository optionBulletinNoteRepository;

    @Inject
    private OptionBulletinNoteSearchRepository optionBulletinNoteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOptionBulletinNoteMockMvc;

    private OptionBulletinNote optionBulletinNote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OptionBulletinNoteResource optionBulletinNoteResource = new OptionBulletinNoteResource();
        ReflectionTestUtils.setField(optionBulletinNoteResource, "optionBulletinNoteSearchRepository", optionBulletinNoteSearchRepository);
        ReflectionTestUtils.setField(optionBulletinNoteResource, "optionBulletinNoteRepository", optionBulletinNoteRepository);
        this.restOptionBulletinNoteMockMvc = MockMvcBuilders.standaloneSetup(optionBulletinNoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        optionBulletinNoteSearchRepository.deleteAll();
        optionBulletinNote = new OptionBulletinNote();
        optionBulletinNote.setNomEnseignant(DEFAULT_NOM_ENSEIGNANT);
        optionBulletinNote.setCoef(DEFAULT_COEF);
        optionBulletinNote.setNoteMin(DEFAULT_NOTE_MIN);
        optionBulletinNote.setNoteMax(DEFAULT_NOTE_MAX);
        optionBulletinNote.setRangMatiere(DEFAULT_RANG_MATIERE);
        optionBulletinNote.setMoyenneMatiere(DEFAULT_MOYENNE_MATIERE);
        optionBulletinNote.setAppreciation(DEFAULT_APPRECIATION);
        optionBulletinNote.setMoyenneGeneraleClasse(DEFAULT_MOYENNE_GENERALE_CLASSE);
        optionBulletinNote.setGroupeMatiere(DEFAULT_GROUPE_MATIERE);
        optionBulletinNote.setPhoto(DEFAULT_PHOTO);
        optionBulletinNote.setTotalEleve(DEFAULT_TOTAL_ELEVE);
        optionBulletinNote.setAfficherSanction(DEFAULT_AFFICHER_SANCTION);
        optionBulletinNote.setAfficherMatricule(DEFAULT_AFFICHER_MATRICULE);
    }

    @Test
    @Transactional
    public void createOptionBulletinNote() throws Exception {
        int databaseSizeBeforeCreate = optionBulletinNoteRepository.findAll().size();

        // Create the OptionBulletinNote

        restOptionBulletinNoteMockMvc.perform(post("/api/option-bulletin-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(optionBulletinNote)))
                .andExpect(status().isCreated());

        // Validate the OptionBulletinNote in the database
        List<OptionBulletinNote> optionBulletinNotes = optionBulletinNoteRepository.findAll();
        assertThat(optionBulletinNotes).hasSize(databaseSizeBeforeCreate + 1);
        OptionBulletinNote testOptionBulletinNote = optionBulletinNotes.get(optionBulletinNotes.size() - 1);
        assertThat(testOptionBulletinNote.isNomEnseignant()).isEqualTo(DEFAULT_NOM_ENSEIGNANT);
        assertThat(testOptionBulletinNote.isCoef()).isEqualTo(DEFAULT_COEF);
        assertThat(testOptionBulletinNote.isNoteMin()).isEqualTo(DEFAULT_NOTE_MIN);
        assertThat(testOptionBulletinNote.isNoteMax()).isEqualTo(DEFAULT_NOTE_MAX);
        assertThat(testOptionBulletinNote.isRangMatiere()).isEqualTo(DEFAULT_RANG_MATIERE);
        assertThat(testOptionBulletinNote.isMoyenneMatiere()).isEqualTo(DEFAULT_MOYENNE_MATIERE);
        assertThat(testOptionBulletinNote.isAppreciation()).isEqualTo(DEFAULT_APPRECIATION);
        assertThat(testOptionBulletinNote.isMoyenneGeneraleClasse()).isEqualTo(DEFAULT_MOYENNE_GENERALE_CLASSE);
        assertThat(testOptionBulletinNote.isGroupeMatiere()).isEqualTo(DEFAULT_GROUPE_MATIERE);
        assertThat(testOptionBulletinNote.isPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testOptionBulletinNote.isTotalEleve()).isEqualTo(DEFAULT_TOTAL_ELEVE);
        assertThat(testOptionBulletinNote.isAfficherSanction()).isEqualTo(DEFAULT_AFFICHER_SANCTION);
        assertThat(testOptionBulletinNote.isAfficherMatricule()).isEqualTo(DEFAULT_AFFICHER_MATRICULE);

        // Validate the OptionBulletinNote in ElasticSearch
        OptionBulletinNote optionBulletinNoteEs = optionBulletinNoteSearchRepository.findOne(testOptionBulletinNote.getId());
        assertThat(optionBulletinNoteEs).isEqualToComparingFieldByField(testOptionBulletinNote);
    }

    @Test
    @Transactional
    public void getAllOptionBulletinNotes() throws Exception {
        // Initialize the database
        optionBulletinNoteRepository.saveAndFlush(optionBulletinNote);

        // Get all the optionBulletinNotes
        restOptionBulletinNoteMockMvc.perform(get("/api/option-bulletin-notes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(optionBulletinNote.getId().intValue())))
                .andExpect(jsonPath("$.[*].nomEnseignant").value(hasItem(DEFAULT_NOM_ENSEIGNANT.booleanValue())))
                .andExpect(jsonPath("$.[*].coef").value(hasItem(DEFAULT_COEF.booleanValue())))
                .andExpect(jsonPath("$.[*].noteMin").value(hasItem(DEFAULT_NOTE_MIN.booleanValue())))
                .andExpect(jsonPath("$.[*].noteMax").value(hasItem(DEFAULT_NOTE_MAX.booleanValue())))
                .andExpect(jsonPath("$.[*].rangMatiere").value(hasItem(DEFAULT_RANG_MATIERE.booleanValue())))
                .andExpect(jsonPath("$.[*].moyenneMatiere").value(hasItem(DEFAULT_MOYENNE_MATIERE.booleanValue())))
                .andExpect(jsonPath("$.[*].appreciation").value(hasItem(DEFAULT_APPRECIATION.booleanValue())))
                .andExpect(jsonPath("$.[*].moyenneGeneraleClasse").value(hasItem(DEFAULT_MOYENNE_GENERALE_CLASSE.booleanValue())))
                .andExpect(jsonPath("$.[*].groupeMatiere").value(hasItem(DEFAULT_GROUPE_MATIERE.booleanValue())))
                .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO.booleanValue())))
                .andExpect(jsonPath("$.[*].totalEleve").value(hasItem(DEFAULT_TOTAL_ELEVE.booleanValue())))
                .andExpect(jsonPath("$.[*].afficherSanction").value(hasItem(DEFAULT_AFFICHER_SANCTION.booleanValue())))
                .andExpect(jsonPath("$.[*].afficherMatricule").value(hasItem(DEFAULT_AFFICHER_MATRICULE.booleanValue())));
    }

    @Test
    @Transactional
    public void getOptionBulletinNote() throws Exception {
        // Initialize the database
        optionBulletinNoteRepository.saveAndFlush(optionBulletinNote);

        // Get the optionBulletinNote
        restOptionBulletinNoteMockMvc.perform(get("/api/option-bulletin-notes/{id}", optionBulletinNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(optionBulletinNote.getId().intValue()))
            .andExpect(jsonPath("$.nomEnseignant").value(DEFAULT_NOM_ENSEIGNANT.booleanValue()))
            .andExpect(jsonPath("$.coef").value(DEFAULT_COEF.booleanValue()))
            .andExpect(jsonPath("$.noteMin").value(DEFAULT_NOTE_MIN.booleanValue()))
            .andExpect(jsonPath("$.noteMax").value(DEFAULT_NOTE_MAX.booleanValue()))
            .andExpect(jsonPath("$.rangMatiere").value(DEFAULT_RANG_MATIERE.booleanValue()))
            .andExpect(jsonPath("$.moyenneMatiere").value(DEFAULT_MOYENNE_MATIERE.booleanValue()))
            .andExpect(jsonPath("$.appreciation").value(DEFAULT_APPRECIATION.booleanValue()))
            .andExpect(jsonPath("$.moyenneGeneraleClasse").value(DEFAULT_MOYENNE_GENERALE_CLASSE.booleanValue()))
            .andExpect(jsonPath("$.groupeMatiere").value(DEFAULT_GROUPE_MATIERE.booleanValue()))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO.booleanValue()))
            .andExpect(jsonPath("$.totalEleve").value(DEFAULT_TOTAL_ELEVE.booleanValue()))
            .andExpect(jsonPath("$.afficherSanction").value(DEFAULT_AFFICHER_SANCTION.booleanValue()))
            .andExpect(jsonPath("$.afficherMatricule").value(DEFAULT_AFFICHER_MATRICULE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOptionBulletinNote() throws Exception {
        // Get the optionBulletinNote
        restOptionBulletinNoteMockMvc.perform(get("/api/option-bulletin-notes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOptionBulletinNote() throws Exception {
        // Initialize the database
        optionBulletinNoteRepository.saveAndFlush(optionBulletinNote);
        optionBulletinNoteSearchRepository.save(optionBulletinNote);
        int databaseSizeBeforeUpdate = optionBulletinNoteRepository.findAll().size();

        // Update the optionBulletinNote
        OptionBulletinNote updatedOptionBulletinNote = new OptionBulletinNote();
        updatedOptionBulletinNote.setId(optionBulletinNote.getId());
        updatedOptionBulletinNote.setNomEnseignant(UPDATED_NOM_ENSEIGNANT);
        updatedOptionBulletinNote.setCoef(UPDATED_COEF);
        updatedOptionBulletinNote.setNoteMin(UPDATED_NOTE_MIN);
        updatedOptionBulletinNote.setNoteMax(UPDATED_NOTE_MAX);
        updatedOptionBulletinNote.setRangMatiere(UPDATED_RANG_MATIERE);
        updatedOptionBulletinNote.setMoyenneMatiere(UPDATED_MOYENNE_MATIERE);
        updatedOptionBulletinNote.setAppreciation(UPDATED_APPRECIATION);
        updatedOptionBulletinNote.setMoyenneGeneraleClasse(UPDATED_MOYENNE_GENERALE_CLASSE);
        updatedOptionBulletinNote.setGroupeMatiere(UPDATED_GROUPE_MATIERE);
        updatedOptionBulletinNote.setPhoto(UPDATED_PHOTO);
        updatedOptionBulletinNote.setTotalEleve(UPDATED_TOTAL_ELEVE);
        updatedOptionBulletinNote.setAfficherSanction(UPDATED_AFFICHER_SANCTION);
        updatedOptionBulletinNote.setAfficherMatricule(UPDATED_AFFICHER_MATRICULE);

        restOptionBulletinNoteMockMvc.perform(put("/api/option-bulletin-notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOptionBulletinNote)))
                .andExpect(status().isOk());

        // Validate the OptionBulletinNote in the database
        List<OptionBulletinNote> optionBulletinNotes = optionBulletinNoteRepository.findAll();
        assertThat(optionBulletinNotes).hasSize(databaseSizeBeforeUpdate);
        OptionBulletinNote testOptionBulletinNote = optionBulletinNotes.get(optionBulletinNotes.size() - 1);
        assertThat(testOptionBulletinNote.isNomEnseignant()).isEqualTo(UPDATED_NOM_ENSEIGNANT);
        assertThat(testOptionBulletinNote.isCoef()).isEqualTo(UPDATED_COEF);
        assertThat(testOptionBulletinNote.isNoteMin()).isEqualTo(UPDATED_NOTE_MIN);
        assertThat(testOptionBulletinNote.isNoteMax()).isEqualTo(UPDATED_NOTE_MAX);
        assertThat(testOptionBulletinNote.isRangMatiere()).isEqualTo(UPDATED_RANG_MATIERE);
        assertThat(testOptionBulletinNote.isMoyenneMatiere()).isEqualTo(UPDATED_MOYENNE_MATIERE);
        assertThat(testOptionBulletinNote.isAppreciation()).isEqualTo(UPDATED_APPRECIATION);
        assertThat(testOptionBulletinNote.isMoyenneGeneraleClasse()).isEqualTo(UPDATED_MOYENNE_GENERALE_CLASSE);
        assertThat(testOptionBulletinNote.isGroupeMatiere()).isEqualTo(UPDATED_GROUPE_MATIERE);
        assertThat(testOptionBulletinNote.isPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testOptionBulletinNote.isTotalEleve()).isEqualTo(UPDATED_TOTAL_ELEVE);
        assertThat(testOptionBulletinNote.isAfficherSanction()).isEqualTo(UPDATED_AFFICHER_SANCTION);
        assertThat(testOptionBulletinNote.isAfficherMatricule()).isEqualTo(UPDATED_AFFICHER_MATRICULE);

        // Validate the OptionBulletinNote in ElasticSearch
        OptionBulletinNote optionBulletinNoteEs = optionBulletinNoteSearchRepository.findOne(testOptionBulletinNote.getId());
        assertThat(optionBulletinNoteEs).isEqualToComparingFieldByField(testOptionBulletinNote);
    }

    @Test
    @Transactional
    public void deleteOptionBulletinNote() throws Exception {
        // Initialize the database
        optionBulletinNoteRepository.saveAndFlush(optionBulletinNote);
        optionBulletinNoteSearchRepository.save(optionBulletinNote);
        int databaseSizeBeforeDelete = optionBulletinNoteRepository.findAll().size();

        // Get the optionBulletinNote
        restOptionBulletinNoteMockMvc.perform(delete("/api/option-bulletin-notes/{id}", optionBulletinNote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean optionBulletinNoteExistsInEs = optionBulletinNoteSearchRepository.exists(optionBulletinNote.getId());
        assertThat(optionBulletinNoteExistsInEs).isFalse();

        // Validate the database is empty
        List<OptionBulletinNote> optionBulletinNotes = optionBulletinNoteRepository.findAll();
        assertThat(optionBulletinNotes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOptionBulletinNote() throws Exception {
        // Initialize the database
        optionBulletinNoteRepository.saveAndFlush(optionBulletinNote);
        optionBulletinNoteSearchRepository.save(optionBulletinNote);

        // Search the optionBulletinNote
        restOptionBulletinNoteMockMvc.perform(get("/api/_search/option-bulletin-notes?query=id:" + optionBulletinNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionBulletinNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEnseignant").value(hasItem(DEFAULT_NOM_ENSEIGNANT.booleanValue())))
            .andExpect(jsonPath("$.[*].coef").value(hasItem(DEFAULT_COEF.booleanValue())))
            .andExpect(jsonPath("$.[*].noteMin").value(hasItem(DEFAULT_NOTE_MIN.booleanValue())))
            .andExpect(jsonPath("$.[*].noteMax").value(hasItem(DEFAULT_NOTE_MAX.booleanValue())))
            .andExpect(jsonPath("$.[*].rangMatiere").value(hasItem(DEFAULT_RANG_MATIERE.booleanValue())))
            .andExpect(jsonPath("$.[*].moyenneMatiere").value(hasItem(DEFAULT_MOYENNE_MATIERE.booleanValue())))
            .andExpect(jsonPath("$.[*].appreciation").value(hasItem(DEFAULT_APPRECIATION.booleanValue())))
            .andExpect(jsonPath("$.[*].moyenneGeneraleClasse").value(hasItem(DEFAULT_MOYENNE_GENERALE_CLASSE.booleanValue())))
            .andExpect(jsonPath("$.[*].groupeMatiere").value(hasItem(DEFAULT_GROUPE_MATIERE.booleanValue())))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO.booleanValue())))
            .andExpect(jsonPath("$.[*].totalEleve").value(hasItem(DEFAULT_TOTAL_ELEVE.booleanValue())))
            .andExpect(jsonPath("$.[*].afficherSanction").value(hasItem(DEFAULT_AFFICHER_SANCTION.booleanValue())))
            .andExpect(jsonPath("$.[*].afficherMatricule").value(hasItem(DEFAULT_AFFICHER_MATRICULE.booleanValue())));
    }
}
