package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Parent;
import com.afrologix.skulman.repository.ParentRepository;
import com.afrologix.skulman.repository.search.ParentSearchRepository;

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
 * Test class for the ParentResource REST controller.
 *
 * @see ParentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class ParentResourceIntTest {

    private static final String DEFAULT_NOM_COMPLET_PERE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOM_COMPLET_PERE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_NOM_COMPLET_MERE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOM_COMPLET_MERE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_EMAIL_PERE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMAIL_PERE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_EMAIL_MERE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMAIL_MERE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_TEL_PERE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TEL_PERE = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_TEL_MERE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TEL_MERE = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PROFESSION_PERE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PROFESSION_PERE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PROFESSION_MERE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PROFESSION_MERE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_NOM_COMPLET_TUTEUR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOM_COMPLET_TUTEUR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_EMAIL_TUTEUR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMAIL_TUTEUR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_TEL_TUTEUR = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TEL_TUTEUR = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PROFESSION_TUTEUR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PROFESSION_TUTEUR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private ParentRepository parentRepository;

    @Inject
    private ParentSearchRepository parentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restParentMockMvc;

    private Parent parent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParentResource parentResource = new ParentResource();
        ReflectionTestUtils.setField(parentResource, "parentSearchRepository", parentSearchRepository);
        ReflectionTestUtils.setField(parentResource, "parentRepository", parentRepository);
        this.restParentMockMvc = MockMvcBuilders.standaloneSetup(parentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        parentSearchRepository.deleteAll();
        parent = new Parent();
        parent.setNomCompletPere(DEFAULT_NOM_COMPLET_PERE);
        parent.setNomCompletMere(DEFAULT_NOM_COMPLET_MERE);
        parent.setEmailPere(DEFAULT_EMAIL_PERE);
        parent.setEmailMere(DEFAULT_EMAIL_MERE);
        parent.setTelPere(DEFAULT_TEL_PERE);
        parent.setTelMere(DEFAULT_TEL_MERE);
        parent.setProfessionPere(DEFAULT_PROFESSION_PERE);
        parent.setProfessionMere(DEFAULT_PROFESSION_MERE);
        parent.setNomCompletTuteur(DEFAULT_NOM_COMPLET_TUTEUR);
        parent.setEmailTuteur(DEFAULT_EMAIL_TUTEUR);
        parent.setTelTuteur(DEFAULT_TEL_TUTEUR);
        parent.setProfessionTuteur(DEFAULT_PROFESSION_TUTEUR);
    }

    @Test
    @Transactional
    public void createParent() throws Exception {
        int databaseSizeBeforeCreate = parentRepository.findAll().size();

        // Create the Parent

        restParentMockMvc.perform(post("/api/parents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parent)))
                .andExpect(status().isCreated());

        // Validate the Parent in the database
        List<Parent> parents = parentRepository.findAll();
        assertThat(parents).hasSize(databaseSizeBeforeCreate + 1);
        Parent testParent = parents.get(parents.size() - 1);
        assertThat(testParent.getNomCompletPere()).isEqualTo(DEFAULT_NOM_COMPLET_PERE);
        assertThat(testParent.getNomCompletMere()).isEqualTo(DEFAULT_NOM_COMPLET_MERE);
        assertThat(testParent.getEmailPere()).isEqualTo(DEFAULT_EMAIL_PERE);
        assertThat(testParent.getEmailMere()).isEqualTo(DEFAULT_EMAIL_MERE);
        assertThat(testParent.getTelPere()).isEqualTo(DEFAULT_TEL_PERE);
        assertThat(testParent.getTelMere()).isEqualTo(DEFAULT_TEL_MERE);
        assertThat(testParent.getProfessionPere()).isEqualTo(DEFAULT_PROFESSION_PERE);
        assertThat(testParent.getProfessionMere()).isEqualTo(DEFAULT_PROFESSION_MERE);
        assertThat(testParent.getNomCompletTuteur()).isEqualTo(DEFAULT_NOM_COMPLET_TUTEUR);
        assertThat(testParent.getEmailTuteur()).isEqualTo(DEFAULT_EMAIL_TUTEUR);
        assertThat(testParent.getTelTuteur()).isEqualTo(DEFAULT_TEL_TUTEUR);
        assertThat(testParent.getProfessionTuteur()).isEqualTo(DEFAULT_PROFESSION_TUTEUR);

        // Validate the Parent in ElasticSearch
        Parent parentEs = parentSearchRepository.findOne(testParent.getId());
        assertThat(parentEs).isEqualToComparingFieldByField(testParent);
    }

    @Test
    @Transactional
    public void getAllParents() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parents
        restParentMockMvc.perform(get("/api/parents?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(parent.getId().intValue())))
                .andExpect(jsonPath("$.[*].nomCompletPere").value(hasItem(DEFAULT_NOM_COMPLET_PERE.toString())))
                .andExpect(jsonPath("$.[*].nomCompletMere").value(hasItem(DEFAULT_NOM_COMPLET_MERE.toString())))
                .andExpect(jsonPath("$.[*].emailPere").value(hasItem(DEFAULT_EMAIL_PERE.toString())))
                .andExpect(jsonPath("$.[*].emailMere").value(hasItem(DEFAULT_EMAIL_MERE.toString())))
                .andExpect(jsonPath("$.[*].telPere").value(hasItem(DEFAULT_TEL_PERE.toString())))
                .andExpect(jsonPath("$.[*].telMere").value(hasItem(DEFAULT_TEL_MERE.toString())))
                .andExpect(jsonPath("$.[*].professionPere").value(hasItem(DEFAULT_PROFESSION_PERE.toString())))
                .andExpect(jsonPath("$.[*].professionMere").value(hasItem(DEFAULT_PROFESSION_MERE.toString())))
                .andExpect(jsonPath("$.[*].nomCompletTuteur").value(hasItem(DEFAULT_NOM_COMPLET_TUTEUR.toString())))
                .andExpect(jsonPath("$.[*].emailTuteur").value(hasItem(DEFAULT_EMAIL_TUTEUR.toString())))
                .andExpect(jsonPath("$.[*].telTuteur").value(hasItem(DEFAULT_TEL_TUTEUR.toString())))
                .andExpect(jsonPath("$.[*].professionTuteur").value(hasItem(DEFAULT_PROFESSION_TUTEUR.toString())));
    }

    @Test
    @Transactional
    public void getParent() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get the parent
        restParentMockMvc.perform(get("/api/parents/{id}", parent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(parent.getId().intValue()))
            .andExpect(jsonPath("$.nomCompletPere").value(DEFAULT_NOM_COMPLET_PERE.toString()))
            .andExpect(jsonPath("$.nomCompletMere").value(DEFAULT_NOM_COMPLET_MERE.toString()))
            .andExpect(jsonPath("$.emailPere").value(DEFAULT_EMAIL_PERE.toString()))
            .andExpect(jsonPath("$.emailMere").value(DEFAULT_EMAIL_MERE.toString()))
            .andExpect(jsonPath("$.telPere").value(DEFAULT_TEL_PERE.toString()))
            .andExpect(jsonPath("$.telMere").value(DEFAULT_TEL_MERE.toString()))
            .andExpect(jsonPath("$.professionPere").value(DEFAULT_PROFESSION_PERE.toString()))
            .andExpect(jsonPath("$.professionMere").value(DEFAULT_PROFESSION_MERE.toString()))
            .andExpect(jsonPath("$.nomCompletTuteur").value(DEFAULT_NOM_COMPLET_TUTEUR.toString()))
            .andExpect(jsonPath("$.emailTuteur").value(DEFAULT_EMAIL_TUTEUR.toString()))
            .andExpect(jsonPath("$.telTuteur").value(DEFAULT_TEL_TUTEUR.toString()))
            .andExpect(jsonPath("$.professionTuteur").value(DEFAULT_PROFESSION_TUTEUR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParent() throws Exception {
        // Get the parent
        restParentMockMvc.perform(get("/api/parents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParent() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);
        parentSearchRepository.save(parent);
        int databaseSizeBeforeUpdate = parentRepository.findAll().size();

        // Update the parent
        Parent updatedParent = new Parent();
        updatedParent.setId(parent.getId());
        updatedParent.setNomCompletPere(UPDATED_NOM_COMPLET_PERE);
        updatedParent.setNomCompletMere(UPDATED_NOM_COMPLET_MERE);
        updatedParent.setEmailPere(UPDATED_EMAIL_PERE);
        updatedParent.setEmailMere(UPDATED_EMAIL_MERE);
        updatedParent.setTelPere(UPDATED_TEL_PERE);
        updatedParent.setTelMere(UPDATED_TEL_MERE);
        updatedParent.setProfessionPere(UPDATED_PROFESSION_PERE);
        updatedParent.setProfessionMere(UPDATED_PROFESSION_MERE);
        updatedParent.setNomCompletTuteur(UPDATED_NOM_COMPLET_TUTEUR);
        updatedParent.setEmailTuteur(UPDATED_EMAIL_TUTEUR);
        updatedParent.setTelTuteur(UPDATED_TEL_TUTEUR);
        updatedParent.setProfessionTuteur(UPDATED_PROFESSION_TUTEUR);

        restParentMockMvc.perform(put("/api/parents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedParent)))
                .andExpect(status().isOk());

        // Validate the Parent in the database
        List<Parent> parents = parentRepository.findAll();
        assertThat(parents).hasSize(databaseSizeBeforeUpdate);
        Parent testParent = parents.get(parents.size() - 1);
        assertThat(testParent.getNomCompletPere()).isEqualTo(UPDATED_NOM_COMPLET_PERE);
        assertThat(testParent.getNomCompletMere()).isEqualTo(UPDATED_NOM_COMPLET_MERE);
        assertThat(testParent.getEmailPere()).isEqualTo(UPDATED_EMAIL_PERE);
        assertThat(testParent.getEmailMere()).isEqualTo(UPDATED_EMAIL_MERE);
        assertThat(testParent.getTelPere()).isEqualTo(UPDATED_TEL_PERE);
        assertThat(testParent.getTelMere()).isEqualTo(UPDATED_TEL_MERE);
        assertThat(testParent.getProfessionPere()).isEqualTo(UPDATED_PROFESSION_PERE);
        assertThat(testParent.getProfessionMere()).isEqualTo(UPDATED_PROFESSION_MERE);
        assertThat(testParent.getNomCompletTuteur()).isEqualTo(UPDATED_NOM_COMPLET_TUTEUR);
        assertThat(testParent.getEmailTuteur()).isEqualTo(UPDATED_EMAIL_TUTEUR);
        assertThat(testParent.getTelTuteur()).isEqualTo(UPDATED_TEL_TUTEUR);
        assertThat(testParent.getProfessionTuteur()).isEqualTo(UPDATED_PROFESSION_TUTEUR);

        // Validate the Parent in ElasticSearch
        Parent parentEs = parentSearchRepository.findOne(testParent.getId());
        assertThat(parentEs).isEqualToComparingFieldByField(testParent);
    }

    @Test
    @Transactional
    public void deleteParent() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);
        parentSearchRepository.save(parent);
        int databaseSizeBeforeDelete = parentRepository.findAll().size();

        // Get the parent
        restParentMockMvc.perform(delete("/api/parents/{id}", parent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean parentExistsInEs = parentSearchRepository.exists(parent.getId());
        assertThat(parentExistsInEs).isFalse();

        // Validate the database is empty
        List<Parent> parents = parentRepository.findAll();
        assertThat(parents).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchParent() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);
        parentSearchRepository.save(parent);

        // Search the parent
        restParentMockMvc.perform(get("/api/_search/parents?query=id:" + parent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parent.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCompletPere").value(hasItem(DEFAULT_NOM_COMPLET_PERE.toString())))
            .andExpect(jsonPath("$.[*].nomCompletMere").value(hasItem(DEFAULT_NOM_COMPLET_MERE.toString())))
            .andExpect(jsonPath("$.[*].emailPere").value(hasItem(DEFAULT_EMAIL_PERE.toString())))
            .andExpect(jsonPath("$.[*].emailMere").value(hasItem(DEFAULT_EMAIL_MERE.toString())))
            .andExpect(jsonPath("$.[*].telPere").value(hasItem(DEFAULT_TEL_PERE.toString())))
            .andExpect(jsonPath("$.[*].telMere").value(hasItem(DEFAULT_TEL_MERE.toString())))
            .andExpect(jsonPath("$.[*].professionPere").value(hasItem(DEFAULT_PROFESSION_PERE.toString())))
            .andExpect(jsonPath("$.[*].professionMere").value(hasItem(DEFAULT_PROFESSION_MERE.toString())))
            .andExpect(jsonPath("$.[*].nomCompletTuteur").value(hasItem(DEFAULT_NOM_COMPLET_TUTEUR.toString())))
            .andExpect(jsonPath("$.[*].emailTuteur").value(hasItem(DEFAULT_EMAIL_TUTEUR.toString())))
            .andExpect(jsonPath("$.[*].telTuteur").value(hasItem(DEFAULT_TEL_TUTEUR.toString())))
            .andExpect(jsonPath("$.[*].professionTuteur").value(hasItem(DEFAULT_PROFESSION_TUTEUR.toString())));
    }
}
