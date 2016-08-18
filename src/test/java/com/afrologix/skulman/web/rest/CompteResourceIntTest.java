package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Compte;
import com.afrologix.skulman.repository.CompteRepository;
import com.afrologix.skulman.repository.search.CompteSearchRepository;

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
 * Test class for the CompteResource REST controller.
 *
 * @see CompteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class CompteResourceIntTest {


    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private CompteRepository compteRepository;

    @Inject
    private CompteSearchRepository compteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompteMockMvc;

    private Compte compte;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompteResource compteResource = new CompteResource();
        ReflectionTestUtils.setField(compteResource, "compteSearchRepository", compteSearchRepository);
        ReflectionTestUtils.setField(compteResource, "compteRepository", compteRepository);
        this.restCompteMockMvc = MockMvcBuilders.standaloneSetup(compteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        compteSearchRepository.deleteAll();
        compte = new Compte();
        compte.setMontant(DEFAULT_MONTANT);
        compte.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createCompte() throws Exception {
        int databaseSizeBeforeCreate = compteRepository.findAll().size();

        // Create the Compte

        restCompteMockMvc.perform(post("/api/comptes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(compte)))
                .andExpect(status().isCreated());

        // Validate the Compte in the database
        List<Compte> comptes = compteRepository.findAll();
        assertThat(comptes).hasSize(databaseSizeBeforeCreate + 1);
        Compte testCompte = comptes.get(comptes.size() - 1);
        assertThat(testCompte.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testCompte.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the Compte in ElasticSearch
        Compte compteEs = compteSearchRepository.findOne(testCompte.getId());
        assertThat(compteEs).isEqualToComparingFieldByField(testCompte);
    }

    @Test
    @Transactional
    public void getAllComptes() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the comptes
        restCompteMockMvc.perform(get("/api/comptes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(compte.getId().intValue())))
                .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getCompte() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get the compte
        restCompteMockMvc.perform(get("/api/comptes/{id}", compte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(compte.getId().intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompte() throws Exception {
        // Get the compte
        restCompteMockMvc.perform(get("/api/comptes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompte() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);
        compteSearchRepository.save(compte);
        int databaseSizeBeforeUpdate = compteRepository.findAll().size();

        // Update the compte
        Compte updatedCompte = new Compte();
        updatedCompte.setId(compte.getId());
        updatedCompte.setMontant(UPDATED_MONTANT);
        updatedCompte.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restCompteMockMvc.perform(put("/api/comptes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCompte)))
                .andExpect(status().isOk());

        // Validate the Compte in the database
        List<Compte> comptes = compteRepository.findAll();
        assertThat(comptes).hasSize(databaseSizeBeforeUpdate);
        Compte testCompte = comptes.get(comptes.size() - 1);
        assertThat(testCompte.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testCompte.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the Compte in ElasticSearch
        Compte compteEs = compteSearchRepository.findOne(testCompte.getId());
        assertThat(compteEs).isEqualToComparingFieldByField(testCompte);
    }

    @Test
    @Transactional
    public void deleteCompte() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);
        compteSearchRepository.save(compte);
        int databaseSizeBeforeDelete = compteRepository.findAll().size();

        // Get the compte
        restCompteMockMvc.perform(delete("/api/comptes/{id}", compte.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean compteExistsInEs = compteSearchRepository.exists(compte.getId());
        assertThat(compteExistsInEs).isFalse();

        // Validate the database is empty
        List<Compte> comptes = compteRepository.findAll();
        assertThat(comptes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompte() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);
        compteSearchRepository.save(compte);

        // Search the compte
        restCompteMockMvc.perform(get("/api/_search/comptes?query=id:" + compte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compte.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
