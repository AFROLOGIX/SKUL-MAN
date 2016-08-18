package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Eleve;
import com.afrologix.skulman.repository.EleveRepository;
import com.afrologix.skulman.repository.search.EleveSearchRepository;

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
 * Test class for the EleveResource REST controller.
 *
 * @see EleveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class EleveResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_NOM = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PRENOM = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISS = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LIEU_NAISS = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISS = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_SEXE = "A";
    private static final String UPDATED_SEXE = "B";
    private static final String DEFAULT_TEL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_NATIONALITE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NATIONALITE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_EMAIL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
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
    private EleveRepository eleveRepository;

    @Inject
    private EleveSearchRepository eleveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEleveMockMvc;

    private Eleve eleve;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EleveResource eleveResource = new EleveResource();
        ReflectionTestUtils.setField(eleveResource, "eleveSearchRepository", eleveSearchRepository);
        ReflectionTestUtils.setField(eleveResource, "eleveRepository", eleveRepository);
        this.restEleveMockMvc = MockMvcBuilders.standaloneSetup(eleveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        eleveSearchRepository.deleteAll();
        eleve = new Eleve();
        eleve.setMatricule(DEFAULT_MATRICULE);
        eleve.setNom(DEFAULT_NOM);
        eleve.setPrenom(DEFAULT_PRENOM);
        eleve.setDateNaiss(DEFAULT_DATE_NAISS);
        eleve.setLieuNaiss(DEFAULT_LIEU_NAISS);
        eleve.setSexe(DEFAULT_SEXE);
        eleve.setTel(DEFAULT_TEL);
        eleve.setNationalite(DEFAULT_NATIONALITE);
        eleve.setEmail(DEFAULT_EMAIL);
        eleve.setCreateBy(DEFAULT_CREATE_BY);
        eleve.setUpdateBy(DEFAULT_UPDATE_BY);
        eleve.setCreateAt(DEFAULT_CREATE_AT);
        eleve.setUpdateAt(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createEleve() throws Exception {
        int databaseSizeBeforeCreate = eleveRepository.findAll().size();

        // Create the Eleve

        restEleveMockMvc.perform(post("/api/eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eleve)))
                .andExpect(status().isCreated());

        // Validate the Eleve in the database
        List<Eleve> eleves = eleveRepository.findAll();
        assertThat(eleves).hasSize(databaseSizeBeforeCreate + 1);
        Eleve testEleve = eleves.get(eleves.size() - 1);
        assertThat(testEleve.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testEleve.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEleve.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEleve.getDateNaiss()).isEqualTo(DEFAULT_DATE_NAISS);
        assertThat(testEleve.getLieuNaiss()).isEqualTo(DEFAULT_LIEU_NAISS);
        assertThat(testEleve.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEleve.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testEleve.getNationalite()).isEqualTo(DEFAULT_NATIONALITE);
        assertThat(testEleve.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEleve.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testEleve.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testEleve.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testEleve.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the Eleve in ElasticSearch
        Eleve eleveEs = eleveSearchRepository.findOne(testEleve.getId());
        assertThat(eleveEs).isEqualToComparingFieldByField(testEleve);
    }

    @Test
    @Transactional
    public void getAllEleves() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleves
        restEleveMockMvc.perform(get("/api/eleves?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(eleve.getId().intValue())))
                .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.toString())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].dateNaiss").value(hasItem(DEFAULT_DATE_NAISS.toString())))
                .andExpect(jsonPath("$.[*].lieuNaiss").value(hasItem(DEFAULT_LIEU_NAISS.toString())))
                .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
                .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }

    @Test
    @Transactional
    public void getEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get the eleve
        restEleveMockMvc.perform(get("/api/eleves/{id}", eleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(eleve.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.dateNaiss").value(DEFAULT_DATE_NAISS.toString()))
            .andExpect(jsonPath("$.lieuNaiss").value(DEFAULT_LIEU_NAISS.toString()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.nationalite").value(DEFAULT_NATIONALITE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT_STR))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingEleve() throws Exception {
        // Get the eleve
        restEleveMockMvc.perform(get("/api/eleves/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);
        eleveSearchRepository.save(eleve);
        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();

        // Update the eleve
        Eleve updatedEleve = new Eleve();
        updatedEleve.setId(eleve.getId());
        updatedEleve.setMatricule(UPDATED_MATRICULE);
        updatedEleve.setNom(UPDATED_NOM);
        updatedEleve.setPrenom(UPDATED_PRENOM);
        updatedEleve.setDateNaiss(UPDATED_DATE_NAISS);
        updatedEleve.setLieuNaiss(UPDATED_LIEU_NAISS);
        updatedEleve.setSexe(UPDATED_SEXE);
        updatedEleve.setTel(UPDATED_TEL);
        updatedEleve.setNationalite(UPDATED_NATIONALITE);
        updatedEleve.setEmail(UPDATED_EMAIL);
        updatedEleve.setCreateBy(UPDATED_CREATE_BY);
        updatedEleve.setUpdateBy(UPDATED_UPDATE_BY);
        updatedEleve.setCreateAt(UPDATED_CREATE_AT);
        updatedEleve.setUpdateAt(UPDATED_UPDATE_AT);

        restEleveMockMvc.perform(put("/api/eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEleve)))
                .andExpect(status().isOk());

        // Validate the Eleve in the database
        List<Eleve> eleves = eleveRepository.findAll();
        assertThat(eleves).hasSize(databaseSizeBeforeUpdate);
        Eleve testEleve = eleves.get(eleves.size() - 1);
        assertThat(testEleve.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testEleve.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEleve.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEleve.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testEleve.getLieuNaiss()).isEqualTo(UPDATED_LIEU_NAISS);
        assertThat(testEleve.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEleve.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testEleve.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testEleve.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEleve.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testEleve.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testEleve.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testEleve.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the Eleve in ElasticSearch
        Eleve eleveEs = eleveSearchRepository.findOne(testEleve.getId());
        assertThat(eleveEs).isEqualToComparingFieldByField(testEleve);
    }

    @Test
    @Transactional
    public void deleteEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);
        eleveSearchRepository.save(eleve);
        int databaseSizeBeforeDelete = eleveRepository.findAll().size();

        // Get the eleve
        restEleveMockMvc.perform(delete("/api/eleves/{id}", eleve.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean eleveExistsInEs = eleveSearchRepository.exists(eleve.getId());
        assertThat(eleveExistsInEs).isFalse();

        // Validate the database is empty
        List<Eleve> eleves = eleveRepository.findAll();
        assertThat(eleves).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);
        eleveSearchRepository.save(eleve);

        // Search the eleve
        restEleveMockMvc.perform(get("/api/_search/eleves?query=id:" + eleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eleve.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].dateNaiss").value(hasItem(DEFAULT_DATE_NAISS.toString())))
            .andExpect(jsonPath("$.[*].lieuNaiss").value(hasItem(DEFAULT_LIEU_NAISS.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }
}
