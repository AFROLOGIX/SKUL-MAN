package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.AnneeScolaire;
import com.afrologix.skulman.repository.AnneeScolaireRepository;
import com.afrologix.skulman.repository.search.AnneeScolaireSearchRepository;

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
 * Test class for the AnneeScolaireResource REST controller.
 *
 * @see AnneeScolaireResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class AnneeScolaireResourceIntTest {

    private static final String DEFAULT_ANNEE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CODE_ANNEE = "AAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE_ANNEE = "BBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;
    private static final String DEFAULT_OBJECTIFS = "AAAAA";
    private static final String UPDATED_OBJECTIFS = "BBBBB";

    @Inject
    private AnneeScolaireRepository anneeScolaireRepository;

    @Inject
    private AnneeScolaireSearchRepository anneeScolaireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAnneeScolaireMockMvc;

    private AnneeScolaire anneeScolaire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnneeScolaireResource anneeScolaireResource = new AnneeScolaireResource();
        ReflectionTestUtils.setField(anneeScolaireResource, "anneeScolaireSearchRepository", anneeScolaireSearchRepository);
        ReflectionTestUtils.setField(anneeScolaireResource, "anneeScolaireRepository", anneeScolaireRepository);
        this.restAnneeScolaireMockMvc = MockMvcBuilders.standaloneSetup(anneeScolaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        anneeScolaireSearchRepository.deleteAll();
        anneeScolaire = new AnneeScolaire();
        anneeScolaire.setAnnee(DEFAULT_ANNEE);
        anneeScolaire.setCodeAnnee(DEFAULT_CODE_ANNEE);
        anneeScolaire.setDateDeb(DEFAULT_DATE_DEB);
        anneeScolaire.setDateFin(DEFAULT_DATE_FIN);
        anneeScolaire.setIsActive(DEFAULT_IS_ACTIVE);
        anneeScolaire.setObjectifs(DEFAULT_OBJECTIFS);
    }

    @Test
    @Transactional
    public void createAnneeScolaire() throws Exception {
        int databaseSizeBeforeCreate = anneeScolaireRepository.findAll().size();

        // Create the AnneeScolaire

        restAnneeScolaireMockMvc.perform(post("/api/annee-scolaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(anneeScolaire)))
                .andExpect(status().isCreated());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaires = anneeScolaireRepository.findAll();
        assertThat(anneeScolaires).hasSize(databaseSizeBeforeCreate + 1);
        AnneeScolaire testAnneeScolaire = anneeScolaires.get(anneeScolaires.size() - 1);
        assertThat(testAnneeScolaire.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testAnneeScolaire.getCodeAnnee()).isEqualTo(DEFAULT_CODE_ANNEE);
        assertThat(testAnneeScolaire.getDateDeb()).isEqualTo(DEFAULT_DATE_DEB);
        assertThat(testAnneeScolaire.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testAnneeScolaire.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testAnneeScolaire.getObjectifs()).isEqualTo(DEFAULT_OBJECTIFS);

        // Validate the AnneeScolaire in ElasticSearch
        AnneeScolaire anneeScolaireEs = anneeScolaireSearchRepository.findOne(testAnneeScolaire.getId());
        assertThat(anneeScolaireEs).isEqualToComparingFieldByField(testAnneeScolaire);
    }

    @Test
    @Transactional
    public void getAllAnneeScolaires() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);

        // Get all the anneeScolaires
        restAnneeScolaireMockMvc.perform(get("/api/annee-scolaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(anneeScolaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE.toString())))
                .andExpect(jsonPath("$.[*].codeAnnee").value(hasItem(DEFAULT_CODE_ANNEE.toString())))
                .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].objectifs").value(hasItem(DEFAULT_OBJECTIFS.toString())));
    }

    @Test
    @Transactional
    public void getAnneeScolaire() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);

        // Get the anneeScolaire
        restAnneeScolaireMockMvc.perform(get("/api/annee-scolaires/{id}", anneeScolaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(anneeScolaire.getId().intValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE.toString()))
            .andExpect(jsonPath("$.codeAnnee").value(DEFAULT_CODE_ANNEE.toString()))
            .andExpect(jsonPath("$.dateDeb").value(DEFAULT_DATE_DEB.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.objectifs").value(DEFAULT_OBJECTIFS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnneeScolaire() throws Exception {
        // Get the anneeScolaire
        restAnneeScolaireMockMvc.perform(get("/api/annee-scolaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnneeScolaire() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);
        anneeScolaireSearchRepository.save(anneeScolaire);
        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();

        // Update the anneeScolaire
        AnneeScolaire updatedAnneeScolaire = new AnneeScolaire();
        updatedAnneeScolaire.setId(anneeScolaire.getId());
        updatedAnneeScolaire.setAnnee(UPDATED_ANNEE);
        updatedAnneeScolaire.setCodeAnnee(UPDATED_CODE_ANNEE);
        updatedAnneeScolaire.setDateDeb(UPDATED_DATE_DEB);
        updatedAnneeScolaire.setDateFin(UPDATED_DATE_FIN);
        updatedAnneeScolaire.setIsActive(UPDATED_IS_ACTIVE);
        updatedAnneeScolaire.setObjectifs(UPDATED_OBJECTIFS);

        restAnneeScolaireMockMvc.perform(put("/api/annee-scolaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAnneeScolaire)))
                .andExpect(status().isOk());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaires = anneeScolaireRepository.findAll();
        assertThat(anneeScolaires).hasSize(databaseSizeBeforeUpdate);
        AnneeScolaire testAnneeScolaire = anneeScolaires.get(anneeScolaires.size() - 1);
        assertThat(testAnneeScolaire.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testAnneeScolaire.getCodeAnnee()).isEqualTo(UPDATED_CODE_ANNEE);
        assertThat(testAnneeScolaire.getDateDeb()).isEqualTo(UPDATED_DATE_DEB);
        assertThat(testAnneeScolaire.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testAnneeScolaire.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testAnneeScolaire.getObjectifs()).isEqualTo(UPDATED_OBJECTIFS);

        // Validate the AnneeScolaire in ElasticSearch
        AnneeScolaire anneeScolaireEs = anneeScolaireSearchRepository.findOne(testAnneeScolaire.getId());
        assertThat(anneeScolaireEs).isEqualToComparingFieldByField(testAnneeScolaire);
    }

    @Test
    @Transactional
    public void deleteAnneeScolaire() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);
        anneeScolaireSearchRepository.save(anneeScolaire);
        int databaseSizeBeforeDelete = anneeScolaireRepository.findAll().size();

        // Get the anneeScolaire
        restAnneeScolaireMockMvc.perform(delete("/api/annee-scolaires/{id}", anneeScolaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean anneeScolaireExistsInEs = anneeScolaireSearchRepository.exists(anneeScolaire.getId());
        assertThat(anneeScolaireExistsInEs).isFalse();

        // Validate the database is empty
        List<AnneeScolaire> anneeScolaires = anneeScolaireRepository.findAll();
        assertThat(anneeScolaires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAnneeScolaire() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);
        anneeScolaireSearchRepository.save(anneeScolaire);

        // Search the anneeScolaire
        restAnneeScolaireMockMvc.perform(get("/api/_search/annee-scolaires?query=id:" + anneeScolaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anneeScolaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE.toString())))
            .andExpect(jsonPath("$.[*].codeAnnee").value(hasItem(DEFAULT_CODE_ANNEE.toString())))
            .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].objectifs").value(hasItem(DEFAULT_OBJECTIFS.toString())));
    }
}
