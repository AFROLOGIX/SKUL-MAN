package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Etablissement;
import com.afrologix.skulman.repository.EtablissementRepository;
import com.afrologix.skulman.repository.search.EtablissementSearchRepository;

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
 * Test class for the EtablissementResource REST controller.
 *
 * @see EtablissementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class EtablissementResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_TITRE_RESPONSABLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TITRE_RESPONSABLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_VILLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_NOM_REPONSABLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOM_REPONSABLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_SITE_WEB = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_SITE_WEB = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CHEMIN_LOGO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CHEMIN_LOGO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_NB_TRIMESTRE = 10;
    private static final Integer UPDATED_NB_TRIMESTRE = 9;

    private static final Integer DEFAULT_NB_SEQUENCE = 10;
    private static final Integer UPDATED_NB_SEQUENCE = 9;
    private static final String DEFAULT_BP = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_BP = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LOCALISATION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LOCALISATION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_TEL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_EMAIL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_DEVISE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DEVISE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private EtablissementRepository etablissementRepository;

    @Inject
    private EtablissementSearchRepository etablissementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEtablissementMockMvc;

    private Etablissement etablissement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EtablissementResource etablissementResource = new EtablissementResource();
        ReflectionTestUtils.setField(etablissementResource, "etablissementSearchRepository", etablissementSearchRepository);
        ReflectionTestUtils.setField(etablissementResource, "etablissementRepository", etablissementRepository);
        this.restEtablissementMockMvc = MockMvcBuilders.standaloneSetup(etablissementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        etablissementSearchRepository.deleteAll();
        etablissement = new Etablissement();
        etablissement.setCode(DEFAULT_CODE);
        etablissement.setLibelleFr(DEFAULT_LIBELLE_FR);
        etablissement.setLibelleEn(DEFAULT_LIBELLE_EN);
        etablissement.setDateCreation(DEFAULT_DATE_CREATION);
        etablissement.setTitreResponsable(DEFAULT_TITRE_RESPONSABLE);
        etablissement.setVille(DEFAULT_VILLE);
        etablissement.setNomReponsable(DEFAULT_NOM_REPONSABLE);
        etablissement.setSiteWeb(DEFAULT_SITE_WEB);
        etablissement.setCheminLogo(DEFAULT_CHEMIN_LOGO);
        etablissement.setNbTrimestre(DEFAULT_NB_TRIMESTRE);
        etablissement.setNbSequence(DEFAULT_NB_SEQUENCE);
        etablissement.setBp(DEFAULT_BP);
        etablissement.setLocalisation(DEFAULT_LOCALISATION);
        etablissement.setTel(DEFAULT_TEL);
        etablissement.setEmail(DEFAULT_EMAIL);
        etablissement.setDevise(DEFAULT_DEVISE);
    }

    @Test
    @Transactional
    public void createEtablissement() throws Exception {
        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();

        // Create the Etablissement

        restEtablissementMockMvc.perform(post("/api/etablissements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(etablissement)))
                .andExpect(status().isCreated());

        // Validate the Etablissement in the database
        List<Etablissement> etablissements = etablissementRepository.findAll();
        assertThat(etablissements).hasSize(databaseSizeBeforeCreate + 1);
        Etablissement testEtablissement = etablissements.get(etablissements.size() - 1);
        assertThat(testEtablissement.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEtablissement.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testEtablissement.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testEtablissement.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testEtablissement.getTitreResponsable()).isEqualTo(DEFAULT_TITRE_RESPONSABLE);
        assertThat(testEtablissement.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testEtablissement.getNomReponsable()).isEqualTo(DEFAULT_NOM_REPONSABLE);
        assertThat(testEtablissement.getSiteWeb()).isEqualTo(DEFAULT_SITE_WEB);
        assertThat(testEtablissement.getCheminLogo()).isEqualTo(DEFAULT_CHEMIN_LOGO);
        assertThat(testEtablissement.getNbTrimestre()).isEqualTo(DEFAULT_NB_TRIMESTRE);
        assertThat(testEtablissement.getNbSequence()).isEqualTo(DEFAULT_NB_SEQUENCE);
        assertThat(testEtablissement.getBp()).isEqualTo(DEFAULT_BP);
        assertThat(testEtablissement.getLocalisation()).isEqualTo(DEFAULT_LOCALISATION);
        assertThat(testEtablissement.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testEtablissement.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEtablissement.getDevise()).isEqualTo(DEFAULT_DEVISE);

        // Validate the Etablissement in ElasticSearch
        Etablissement etablissementEs = etablissementSearchRepository.findOne(testEtablissement.getId());
        assertThat(etablissementEs).isEqualToComparingFieldByField(testEtablissement);
    }

    @Test
    @Transactional
    public void getAllEtablissements() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissements
        restEtablissementMockMvc.perform(get("/api/etablissements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
                .andExpect(jsonPath("$.[*].titreResponsable").value(hasItem(DEFAULT_TITRE_RESPONSABLE.toString())))
                .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
                .andExpect(jsonPath("$.[*].nomReponsable").value(hasItem(DEFAULT_NOM_REPONSABLE.toString())))
                .andExpect(jsonPath("$.[*].siteWeb").value(hasItem(DEFAULT_SITE_WEB.toString())))
                .andExpect(jsonPath("$.[*].cheminLogo").value(hasItem(DEFAULT_CHEMIN_LOGO.toString())))
                .andExpect(jsonPath("$.[*].nbTrimestre").value(hasItem(DEFAULT_NB_TRIMESTRE)))
                .andExpect(jsonPath("$.[*].nbSequence").value(hasItem(DEFAULT_NB_SEQUENCE)))
                .andExpect(jsonPath("$.[*].bp").value(hasItem(DEFAULT_BP.toString())))
                .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].devise").value(hasItem(DEFAULT_DEVISE.toString())));
    }

    @Test
    @Transactional
    public void getEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get the etablissement
        restEtablissementMockMvc.perform(get("/api/etablissements/{id}", etablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(etablissement.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.titreResponsable").value(DEFAULT_TITRE_RESPONSABLE.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()))
            .andExpect(jsonPath("$.nomReponsable").value(DEFAULT_NOM_REPONSABLE.toString()))
            .andExpect(jsonPath("$.siteWeb").value(DEFAULT_SITE_WEB.toString()))
            .andExpect(jsonPath("$.cheminLogo").value(DEFAULT_CHEMIN_LOGO.toString()))
            .andExpect(jsonPath("$.nbTrimestre").value(DEFAULT_NB_TRIMESTRE))
            .andExpect(jsonPath("$.nbSequence").value(DEFAULT_NB_SEQUENCE))
            .andExpect(jsonPath("$.bp").value(DEFAULT_BP.toString()))
            .andExpect(jsonPath("$.localisation").value(DEFAULT_LOCALISATION.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.devise").value(DEFAULT_DEVISE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEtablissement() throws Exception {
        // Get the etablissement
        restEtablissementMockMvc.perform(get("/api/etablissements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);
        etablissementSearchRepository.save(etablissement);
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Update the etablissement
        Etablissement updatedEtablissement = new Etablissement();
        updatedEtablissement.setId(etablissement.getId());
        updatedEtablissement.setCode(UPDATED_CODE);
        updatedEtablissement.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedEtablissement.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedEtablissement.setDateCreation(UPDATED_DATE_CREATION);
        updatedEtablissement.setTitreResponsable(UPDATED_TITRE_RESPONSABLE);
        updatedEtablissement.setVille(UPDATED_VILLE);
        updatedEtablissement.setNomReponsable(UPDATED_NOM_REPONSABLE);
        updatedEtablissement.setSiteWeb(UPDATED_SITE_WEB);
        updatedEtablissement.setCheminLogo(UPDATED_CHEMIN_LOGO);
        updatedEtablissement.setNbTrimestre(UPDATED_NB_TRIMESTRE);
        updatedEtablissement.setNbSequence(UPDATED_NB_SEQUENCE);
        updatedEtablissement.setBp(UPDATED_BP);
        updatedEtablissement.setLocalisation(UPDATED_LOCALISATION);
        updatedEtablissement.setTel(UPDATED_TEL);
        updatedEtablissement.setEmail(UPDATED_EMAIL);
        updatedEtablissement.setDevise(UPDATED_DEVISE);

        restEtablissementMockMvc.perform(put("/api/etablissements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEtablissement)))
                .andExpect(status().isOk());

        // Validate the Etablissement in the database
        List<Etablissement> etablissements = etablissementRepository.findAll();
        assertThat(etablissements).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissements.get(etablissements.size() - 1);
        assertThat(testEtablissement.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEtablissement.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testEtablissement.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testEtablissement.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testEtablissement.getTitreResponsable()).isEqualTo(UPDATED_TITRE_RESPONSABLE);
        assertThat(testEtablissement.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testEtablissement.getNomReponsable()).isEqualTo(UPDATED_NOM_REPONSABLE);
        assertThat(testEtablissement.getSiteWeb()).isEqualTo(UPDATED_SITE_WEB);
        assertThat(testEtablissement.getCheminLogo()).isEqualTo(UPDATED_CHEMIN_LOGO);
        assertThat(testEtablissement.getNbTrimestre()).isEqualTo(UPDATED_NB_TRIMESTRE);
        assertThat(testEtablissement.getNbSequence()).isEqualTo(UPDATED_NB_SEQUENCE);
        assertThat(testEtablissement.getBp()).isEqualTo(UPDATED_BP);
        assertThat(testEtablissement.getLocalisation()).isEqualTo(UPDATED_LOCALISATION);
        assertThat(testEtablissement.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testEtablissement.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEtablissement.getDevise()).isEqualTo(UPDATED_DEVISE);

        // Validate the Etablissement in ElasticSearch
        Etablissement etablissementEs = etablissementSearchRepository.findOne(testEtablissement.getId());
        assertThat(etablissementEs).isEqualToComparingFieldByField(testEtablissement);
    }

    @Test
    @Transactional
    public void deleteEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);
        etablissementSearchRepository.save(etablissement);
        int databaseSizeBeforeDelete = etablissementRepository.findAll().size();

        // Get the etablissement
        restEtablissementMockMvc.perform(delete("/api/etablissements/{id}", etablissement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean etablissementExistsInEs = etablissementSearchRepository.exists(etablissement.getId());
        assertThat(etablissementExistsInEs).isFalse();

        // Validate the database is empty
        List<Etablissement> etablissements = etablissementRepository.findAll();
        assertThat(etablissements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);
        etablissementSearchRepository.save(etablissement);

        // Search the etablissement
        restEtablissementMockMvc.perform(get("/api/_search/etablissements?query=id:" + etablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].titreResponsable").value(hasItem(DEFAULT_TITRE_RESPONSABLE.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].nomReponsable").value(hasItem(DEFAULT_NOM_REPONSABLE.toString())))
            .andExpect(jsonPath("$.[*].siteWeb").value(hasItem(DEFAULT_SITE_WEB.toString())))
            .andExpect(jsonPath("$.[*].cheminLogo").value(hasItem(DEFAULT_CHEMIN_LOGO.toString())))
            .andExpect(jsonPath("$.[*].nbTrimestre").value(hasItem(DEFAULT_NB_TRIMESTRE)))
            .andExpect(jsonPath("$.[*].nbSequence").value(hasItem(DEFAULT_NB_SEQUENCE)))
            .andExpect(jsonPath("$.[*].bp").value(hasItem(DEFAULT_BP.toString())))
            .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].devise").value(hasItem(DEFAULT_DEVISE.toString())));
    }
}
