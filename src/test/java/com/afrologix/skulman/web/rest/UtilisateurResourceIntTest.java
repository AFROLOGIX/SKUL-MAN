package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Utilisateur;
import com.afrologix.skulman.repository.UtilisateurRepository;
import com.afrologix.skulman.repository.search.UtilisateurSearchRepository;

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
 * Test class for the UtilisateurResource REST controller.
 *
 * @see UtilisateurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class UtilisateurResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PWD = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PWD = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_NOM = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PRENOM = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_TEL = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_EMAIL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CONNEXION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_CONNEXION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_CONNEXION_STR = dateTimeFormatter.format(DEFAULT_DATE_CONNEXION);

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private UtilisateurSearchRepository utilisateurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUtilisateurMockMvc;

    private Utilisateur utilisateur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UtilisateurResource utilisateurResource = new UtilisateurResource();
        ReflectionTestUtils.setField(utilisateurResource, "utilisateurSearchRepository", utilisateurSearchRepository);
        ReflectionTestUtils.setField(utilisateurResource, "utilisateurRepository", utilisateurRepository);
        this.restUtilisateurMockMvc = MockMvcBuilders.standaloneSetup(utilisateurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        utilisateurSearchRepository.deleteAll();
        utilisateur = new Utilisateur();
        utilisateur.setCode(DEFAULT_CODE);
        utilisateur.setPwd(DEFAULT_PWD);
        utilisateur.setNom(DEFAULT_NOM);
        utilisateur.setPrenom(DEFAULT_PRENOM);
        utilisateur.setTel(DEFAULT_TEL);
        utilisateur.setEmail(DEFAULT_EMAIL);
        utilisateur.setDateConnexion(DEFAULT_DATE_CONNEXION);
    }

    @Test
    @Transactional
    public void createUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = utilisateurRepository.findAll().size();

        // Create the Utilisateur

        restUtilisateurMockMvc.perform(post("/api/utilisateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(utilisateur)))
                .andExpect(status().isCreated());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeCreate + 1);
        Utilisateur testUtilisateur = utilisateurs.get(utilisateurs.size() - 1);
        assertThat(testUtilisateur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUtilisateur.getPwd()).isEqualTo(DEFAULT_PWD);
        assertThat(testUtilisateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testUtilisateur.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testUtilisateur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUtilisateur.getDateConnexion()).isEqualTo(DEFAULT_DATE_CONNEXION);

        // Validate the Utilisateur in ElasticSearch
        Utilisateur utilisateurEs = utilisateurSearchRepository.findOne(testUtilisateur.getId());
        assertThat(utilisateurEs).isEqualToComparingFieldByField(testUtilisateur);
    }

    @Test
    @Transactional
    public void getAllUtilisateurs() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurs
        restUtilisateurMockMvc.perform(get("/api/utilisateurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].pwd").value(hasItem(DEFAULT_PWD.toString())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].dateConnexion").value(hasItem(DEFAULT_DATE_CONNEXION_STR)));
    }

    @Test
    @Transactional
    public void getUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get the utilisateur
        restUtilisateurMockMvc.perform(get("/api/utilisateurs/{id}", utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(utilisateur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.pwd").value(DEFAULT_PWD.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.dateConnexion").value(DEFAULT_DATE_CONNEXION_STR));
    }

    @Test
    @Transactional
    public void getNonExistingUtilisateur() throws Exception {
        // Get the utilisateur
        restUtilisateurMockMvc.perform(get("/api/utilisateurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);
        utilisateurSearchRepository.save(utilisateur);
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = new Utilisateur();
        updatedUtilisateur.setId(utilisateur.getId());
        updatedUtilisateur.setCode(UPDATED_CODE);
        updatedUtilisateur.setPwd(UPDATED_PWD);
        updatedUtilisateur.setNom(UPDATED_NOM);
        updatedUtilisateur.setPrenom(UPDATED_PRENOM);
        updatedUtilisateur.setTel(UPDATED_TEL);
        updatedUtilisateur.setEmail(UPDATED_EMAIL);
        updatedUtilisateur.setDateConnexion(UPDATED_DATE_CONNEXION);

        restUtilisateurMockMvc.perform(put("/api/utilisateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUtilisateur)))
                .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurs.get(utilisateurs.size() - 1);
        assertThat(testUtilisateur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUtilisateur.getPwd()).isEqualTo(UPDATED_PWD);
        assertThat(testUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testUtilisateur.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testUtilisateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUtilisateur.getDateConnexion()).isEqualTo(UPDATED_DATE_CONNEXION);

        // Validate the Utilisateur in ElasticSearch
        Utilisateur utilisateurEs = utilisateurSearchRepository.findOne(testUtilisateur.getId());
        assertThat(utilisateurEs).isEqualToComparingFieldByField(testUtilisateur);
    }

    @Test
    @Transactional
    public void deleteUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);
        utilisateurSearchRepository.save(utilisateur);
        int databaseSizeBeforeDelete = utilisateurRepository.findAll().size();

        // Get the utilisateur
        restUtilisateurMockMvc.perform(delete("/api/utilisateurs/{id}", utilisateur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean utilisateurExistsInEs = utilisateurSearchRepository.exists(utilisateur.getId());
        assertThat(utilisateurExistsInEs).isFalse();

        // Validate the database is empty
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);
        utilisateurSearchRepository.save(utilisateur);

        // Search the utilisateur
        restUtilisateurMockMvc.perform(get("/api/_search/utilisateurs?query=id:" + utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].pwd").value(hasItem(DEFAULT_PWD.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].dateConnexion").value(hasItem(DEFAULT_DATE_CONNEXION_STR)));
    }
}
