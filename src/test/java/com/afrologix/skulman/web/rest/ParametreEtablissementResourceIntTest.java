package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.ParametreEtablissement;
import com.afrologix.skulman.repository.ParametreEtablissementRepository;
import com.afrologix.skulman.repository.search.ParametreEtablissementSearchRepository;

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
 * Test class for the ParametreEtablissementResource REST controller.
 *
 * @see ParametreEtablissementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class ParametreEtablissementResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVER_ENREGISTREMENT_BULLETIN_NOTE_BD = false;
    private static final Boolean UPDATED_ACTIVER_ENREGISTREMENT_BULLETIN_NOTE_BD = true;

    private static final ZonedDateTime DEFAULT_HEURE_DEB_COURS = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HEURE_DEB_COURS = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HEURE_DEB_COURS_STR = dateTimeFormatter.format(DEFAULT_HEURE_DEB_COURS);

    private static final ZonedDateTime DEFAULT_HEURE_FIN_COURS = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HEURE_FIN_COURS = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HEURE_FIN_COURS_STR = dateTimeFormatter.format(DEFAULT_HEURE_FIN_COURS);
    private static final String DEFAULT_REGIME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_REGIME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private ParametreEtablissementRepository parametreEtablissementRepository;

    @Inject
    private ParametreEtablissementSearchRepository parametreEtablissementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restParametreEtablissementMockMvc;

    private ParametreEtablissement parametreEtablissement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParametreEtablissementResource parametreEtablissementResource = new ParametreEtablissementResource();
        ReflectionTestUtils.setField(parametreEtablissementResource, "parametreEtablissementSearchRepository", parametreEtablissementSearchRepository);
        ReflectionTestUtils.setField(parametreEtablissementResource, "parametreEtablissementRepository", parametreEtablissementRepository);
        this.restParametreEtablissementMockMvc = MockMvcBuilders.standaloneSetup(parametreEtablissementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        parametreEtablissementSearchRepository.deleteAll();
        parametreEtablissement = new ParametreEtablissement();
        parametreEtablissement.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        parametreEtablissement.setActiverEnregistrementBulletinNoteBd(DEFAULT_ACTIVER_ENREGISTREMENT_BULLETIN_NOTE_BD);
        parametreEtablissement.setHeureDebCours(DEFAULT_HEURE_DEB_COURS);
        parametreEtablissement.setHeureFinCours(DEFAULT_HEURE_FIN_COURS);
        parametreEtablissement.setRegime(DEFAULT_REGIME);
    }

    @Test
    @Transactional
    public void createParametreEtablissement() throws Exception {
        int databaseSizeBeforeCreate = parametreEtablissementRepository.findAll().size();

        // Create the ParametreEtablissement

        restParametreEtablissementMockMvc.perform(post("/api/parametre-etablissements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parametreEtablissement)))
                .andExpect(status().isCreated());

        // Validate the ParametreEtablissement in the database
        List<ParametreEtablissement> parametreEtablissements = parametreEtablissementRepository.findAll();
        assertThat(parametreEtablissements).hasSize(databaseSizeBeforeCreate + 1);
        ParametreEtablissement testParametreEtablissement = parametreEtablissements.get(parametreEtablissements.size() - 1);
        assertThat(testParametreEtablissement.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testParametreEtablissement.isActiverEnregistrementBulletinNoteBd()).isEqualTo(DEFAULT_ACTIVER_ENREGISTREMENT_BULLETIN_NOTE_BD);
        assertThat(testParametreEtablissement.getHeureDebCours()).isEqualTo(DEFAULT_HEURE_DEB_COURS);
        assertThat(testParametreEtablissement.getHeureFinCours()).isEqualTo(DEFAULT_HEURE_FIN_COURS);
        assertThat(testParametreEtablissement.getRegime()).isEqualTo(DEFAULT_REGIME);

        // Validate the ParametreEtablissement in ElasticSearch
        ParametreEtablissement parametreEtablissementEs = parametreEtablissementSearchRepository.findOne(testParametreEtablissement.getId());
        assertThat(parametreEtablissementEs).isEqualToComparingFieldByField(testParametreEtablissement);
    }

    @Test
    @Transactional
    public void getAllParametreEtablissements() throws Exception {
        // Initialize the database
        parametreEtablissementRepository.saveAndFlush(parametreEtablissement);

        // Get all the parametreEtablissements
        restParametreEtablissementMockMvc.perform(get("/api/parametre-etablissements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(parametreEtablissement.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].activerEnregistrementBulletinNoteBd").value(hasItem(DEFAULT_ACTIVER_ENREGISTREMENT_BULLETIN_NOTE_BD.booleanValue())))
                .andExpect(jsonPath("$.[*].heureDebCours").value(hasItem(DEFAULT_HEURE_DEB_COURS_STR)))
                .andExpect(jsonPath("$.[*].heureFinCours").value(hasItem(DEFAULT_HEURE_FIN_COURS_STR)))
                .andExpect(jsonPath("$.[*].regime").value(hasItem(DEFAULT_REGIME.toString())));
    }

    @Test
    @Transactional
    public void getParametreEtablissement() throws Exception {
        // Initialize the database
        parametreEtablissementRepository.saveAndFlush(parametreEtablissement);

        // Get the parametreEtablissement
        restParametreEtablissementMockMvc.perform(get("/api/parametre-etablissements/{id}", parametreEtablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(parametreEtablissement.getId().intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.activerEnregistrementBulletinNoteBd").value(DEFAULT_ACTIVER_ENREGISTREMENT_BULLETIN_NOTE_BD.booleanValue()))
            .andExpect(jsonPath("$.heureDebCours").value(DEFAULT_HEURE_DEB_COURS_STR))
            .andExpect(jsonPath("$.heureFinCours").value(DEFAULT_HEURE_FIN_COURS_STR))
            .andExpect(jsonPath("$.regime").value(DEFAULT_REGIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParametreEtablissement() throws Exception {
        // Get the parametreEtablissement
        restParametreEtablissementMockMvc.perform(get("/api/parametre-etablissements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParametreEtablissement() throws Exception {
        // Initialize the database
        parametreEtablissementRepository.saveAndFlush(parametreEtablissement);
        parametreEtablissementSearchRepository.save(parametreEtablissement);
        int databaseSizeBeforeUpdate = parametreEtablissementRepository.findAll().size();

        // Update the parametreEtablissement
        ParametreEtablissement updatedParametreEtablissement = new ParametreEtablissement();
        updatedParametreEtablissement.setId(parametreEtablissement.getId());
        updatedParametreEtablissement.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedParametreEtablissement.setActiverEnregistrementBulletinNoteBd(UPDATED_ACTIVER_ENREGISTREMENT_BULLETIN_NOTE_BD);
        updatedParametreEtablissement.setHeureDebCours(UPDATED_HEURE_DEB_COURS);
        updatedParametreEtablissement.setHeureFinCours(UPDATED_HEURE_FIN_COURS);
        updatedParametreEtablissement.setRegime(UPDATED_REGIME);

        restParametreEtablissementMockMvc.perform(put("/api/parametre-etablissements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedParametreEtablissement)))
                .andExpect(status().isOk());

        // Validate the ParametreEtablissement in the database
        List<ParametreEtablissement> parametreEtablissements = parametreEtablissementRepository.findAll();
        assertThat(parametreEtablissements).hasSize(databaseSizeBeforeUpdate);
        ParametreEtablissement testParametreEtablissement = parametreEtablissements.get(parametreEtablissements.size() - 1);
        assertThat(testParametreEtablissement.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testParametreEtablissement.isActiverEnregistrementBulletinNoteBd()).isEqualTo(UPDATED_ACTIVER_ENREGISTREMENT_BULLETIN_NOTE_BD);
        assertThat(testParametreEtablissement.getHeureDebCours()).isEqualTo(UPDATED_HEURE_DEB_COURS);
        assertThat(testParametreEtablissement.getHeureFinCours()).isEqualTo(UPDATED_HEURE_FIN_COURS);
        assertThat(testParametreEtablissement.getRegime()).isEqualTo(UPDATED_REGIME);

        // Validate the ParametreEtablissement in ElasticSearch
        ParametreEtablissement parametreEtablissementEs = parametreEtablissementSearchRepository.findOne(testParametreEtablissement.getId());
        assertThat(parametreEtablissementEs).isEqualToComparingFieldByField(testParametreEtablissement);
    }

    @Test
    @Transactional
    public void deleteParametreEtablissement() throws Exception {
        // Initialize the database
        parametreEtablissementRepository.saveAndFlush(parametreEtablissement);
        parametreEtablissementSearchRepository.save(parametreEtablissement);
        int databaseSizeBeforeDelete = parametreEtablissementRepository.findAll().size();

        // Get the parametreEtablissement
        restParametreEtablissementMockMvc.perform(delete("/api/parametre-etablissements/{id}", parametreEtablissement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean parametreEtablissementExistsInEs = parametreEtablissementSearchRepository.exists(parametreEtablissement.getId());
        assertThat(parametreEtablissementExistsInEs).isFalse();

        // Validate the database is empty
        List<ParametreEtablissement> parametreEtablissements = parametreEtablissementRepository.findAll();
        assertThat(parametreEtablissements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchParametreEtablissement() throws Exception {
        // Initialize the database
        parametreEtablissementRepository.saveAndFlush(parametreEtablissement);
        parametreEtablissementSearchRepository.save(parametreEtablissement);

        // Search the parametreEtablissement
        restParametreEtablissementMockMvc.perform(get("/api/_search/parametre-etablissements?query=id:" + parametreEtablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametreEtablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].activerEnregistrementBulletinNoteBd").value(hasItem(DEFAULT_ACTIVER_ENREGISTREMENT_BULLETIN_NOTE_BD.booleanValue())))
            .andExpect(jsonPath("$.[*].heureDebCours").value(hasItem(DEFAULT_HEURE_DEB_COURS_STR)))
            .andExpect(jsonPath("$.[*].heureFinCours").value(hasItem(DEFAULT_HEURE_FIN_COURS_STR)))
            .andExpect(jsonPath("$.[*].regime").value(hasItem(DEFAULT_REGIME.toString())));
    }
}
