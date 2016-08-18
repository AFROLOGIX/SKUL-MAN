package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.GroupeUtilisateur;
import com.afrologix.skulman.repository.GroupeUtilisateurRepository;
import com.afrologix.skulman.repository.search.GroupeUtilisateurSearchRepository;

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
 * Test class for the GroupeUtilisateurResource REST controller.
 *
 * @see GroupeUtilisateurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class GroupeUtilisateurResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private GroupeUtilisateurRepository groupeUtilisateurRepository;

    @Inject
    private GroupeUtilisateurSearchRepository groupeUtilisateurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGroupeUtilisateurMockMvc;

    private GroupeUtilisateur groupeUtilisateur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GroupeUtilisateurResource groupeUtilisateurResource = new GroupeUtilisateurResource();
        ReflectionTestUtils.setField(groupeUtilisateurResource, "groupeUtilisateurSearchRepository", groupeUtilisateurSearchRepository);
        ReflectionTestUtils.setField(groupeUtilisateurResource, "groupeUtilisateurRepository", groupeUtilisateurRepository);
        this.restGroupeUtilisateurMockMvc = MockMvcBuilders.standaloneSetup(groupeUtilisateurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        groupeUtilisateurSearchRepository.deleteAll();
        groupeUtilisateur = new GroupeUtilisateur();
        groupeUtilisateur.setCode(DEFAULT_CODE);
        groupeUtilisateur.setLibelleFr(DEFAULT_LIBELLE_FR);
        groupeUtilisateur.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createGroupeUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = groupeUtilisateurRepository.findAll().size();

        // Create the GroupeUtilisateur

        restGroupeUtilisateurMockMvc.perform(post("/api/groupe-utilisateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(groupeUtilisateur)))
                .andExpect(status().isCreated());

        // Validate the GroupeUtilisateur in the database
        List<GroupeUtilisateur> groupeUtilisateurs = groupeUtilisateurRepository.findAll();
        assertThat(groupeUtilisateurs).hasSize(databaseSizeBeforeCreate + 1);
        GroupeUtilisateur testGroupeUtilisateur = groupeUtilisateurs.get(groupeUtilisateurs.size() - 1);
        assertThat(testGroupeUtilisateur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testGroupeUtilisateur.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testGroupeUtilisateur.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the GroupeUtilisateur in ElasticSearch
        GroupeUtilisateur groupeUtilisateurEs = groupeUtilisateurSearchRepository.findOne(testGroupeUtilisateur.getId());
        assertThat(groupeUtilisateurEs).isEqualToComparingFieldByField(testGroupeUtilisateur);
    }

    @Test
    @Transactional
    public void getAllGroupeUtilisateurs() throws Exception {
        // Initialize the database
        groupeUtilisateurRepository.saveAndFlush(groupeUtilisateur);

        // Get all the groupeUtilisateurs
        restGroupeUtilisateurMockMvc.perform(get("/api/groupe-utilisateurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(groupeUtilisateur.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getGroupeUtilisateur() throws Exception {
        // Initialize the database
        groupeUtilisateurRepository.saveAndFlush(groupeUtilisateur);

        // Get the groupeUtilisateur
        restGroupeUtilisateurMockMvc.perform(get("/api/groupe-utilisateurs/{id}", groupeUtilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(groupeUtilisateur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGroupeUtilisateur() throws Exception {
        // Get the groupeUtilisateur
        restGroupeUtilisateurMockMvc.perform(get("/api/groupe-utilisateurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroupeUtilisateur() throws Exception {
        // Initialize the database
        groupeUtilisateurRepository.saveAndFlush(groupeUtilisateur);
        groupeUtilisateurSearchRepository.save(groupeUtilisateur);
        int databaseSizeBeforeUpdate = groupeUtilisateurRepository.findAll().size();

        // Update the groupeUtilisateur
        GroupeUtilisateur updatedGroupeUtilisateur = new GroupeUtilisateur();
        updatedGroupeUtilisateur.setId(groupeUtilisateur.getId());
        updatedGroupeUtilisateur.setCode(UPDATED_CODE);
        updatedGroupeUtilisateur.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedGroupeUtilisateur.setLibelleEn(UPDATED_LIBELLE_EN);

        restGroupeUtilisateurMockMvc.perform(put("/api/groupe-utilisateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGroupeUtilisateur)))
                .andExpect(status().isOk());

        // Validate the GroupeUtilisateur in the database
        List<GroupeUtilisateur> groupeUtilisateurs = groupeUtilisateurRepository.findAll();
        assertThat(groupeUtilisateurs).hasSize(databaseSizeBeforeUpdate);
        GroupeUtilisateur testGroupeUtilisateur = groupeUtilisateurs.get(groupeUtilisateurs.size() - 1);
        assertThat(testGroupeUtilisateur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testGroupeUtilisateur.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testGroupeUtilisateur.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the GroupeUtilisateur in ElasticSearch
        GroupeUtilisateur groupeUtilisateurEs = groupeUtilisateurSearchRepository.findOne(testGroupeUtilisateur.getId());
        assertThat(groupeUtilisateurEs).isEqualToComparingFieldByField(testGroupeUtilisateur);
    }

    @Test
    @Transactional
    public void deleteGroupeUtilisateur() throws Exception {
        // Initialize the database
        groupeUtilisateurRepository.saveAndFlush(groupeUtilisateur);
        groupeUtilisateurSearchRepository.save(groupeUtilisateur);
        int databaseSizeBeforeDelete = groupeUtilisateurRepository.findAll().size();

        // Get the groupeUtilisateur
        restGroupeUtilisateurMockMvc.perform(delete("/api/groupe-utilisateurs/{id}", groupeUtilisateur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean groupeUtilisateurExistsInEs = groupeUtilisateurSearchRepository.exists(groupeUtilisateur.getId());
        assertThat(groupeUtilisateurExistsInEs).isFalse();

        // Validate the database is empty
        List<GroupeUtilisateur> groupeUtilisateurs = groupeUtilisateurRepository.findAll();
        assertThat(groupeUtilisateurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGroupeUtilisateur() throws Exception {
        // Initialize the database
        groupeUtilisateurRepository.saveAndFlush(groupeUtilisateur);
        groupeUtilisateurSearchRepository.save(groupeUtilisateur);

        // Search the groupeUtilisateur
        restGroupeUtilisateurMockMvc.perform(get("/api/_search/groupe-utilisateurs?query=id:" + groupeUtilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupeUtilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
