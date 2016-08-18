package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Personnel;
import com.afrologix.skulman.repository.PersonnelRepository;
import com.afrologix.skulman.repository.search.PersonnelSearchRepository;

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
 * Test class for the PersonnelResource REST controller.
 *
 * @see PersonnelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class PersonnelResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PRENOM = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_TEL = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_EMAIL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ADMISSION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADMISSION = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;
    private static final String DEFAULT_ADRESSE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private PersonnelRepository personnelRepository;

    @Inject
    private PersonnelSearchRepository personnelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPersonnelMockMvc;

    private Personnel personnel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonnelResource personnelResource = new PersonnelResource();
        ReflectionTestUtils.setField(personnelResource, "personnelSearchRepository", personnelSearchRepository);
        ReflectionTestUtils.setField(personnelResource, "personnelRepository", personnelRepository);
        this.restPersonnelMockMvc = MockMvcBuilders.standaloneSetup(personnelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        personnelSearchRepository.deleteAll();
        personnel = new Personnel();
        personnel.setNom(DEFAULT_NOM);
        personnel.setPrenom(DEFAULT_PRENOM);
        personnel.setTel(DEFAULT_TEL);
        personnel.setEmail(DEFAULT_EMAIL);
        personnel.setDateAdmission(DEFAULT_DATE_ADMISSION);
        personnel.setIsActive(DEFAULT_IS_ACTIVE);
        personnel.setAdresse(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    public void createPersonnel() throws Exception {
        int databaseSizeBeforeCreate = personnelRepository.findAll().size();

        // Create the Personnel

        restPersonnelMockMvc.perform(post("/api/personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personnel)))
                .andExpect(status().isCreated());

        // Validate the Personnel in the database
        List<Personnel> personnels = personnelRepository.findAll();
        assertThat(personnels).hasSize(databaseSizeBeforeCreate + 1);
        Personnel testPersonnel = personnels.get(personnels.size() - 1);
        assertThat(testPersonnel.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPersonnel.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPersonnel.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testPersonnel.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersonnel.getDateAdmission()).isEqualTo(DEFAULT_DATE_ADMISSION);
        assertThat(testPersonnel.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testPersonnel.getAdresse()).isEqualTo(DEFAULT_ADRESSE);

        // Validate the Personnel in ElasticSearch
        Personnel personnelEs = personnelSearchRepository.findOne(testPersonnel.getId());
        assertThat(personnelEs).isEqualToComparingFieldByField(testPersonnel);
    }

    @Test
    @Transactional
    public void getAllPersonnels() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        // Get all the personnels
        restPersonnelMockMvc.perform(get("/api/personnels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(personnel.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].dateAdmission").value(hasItem(DEFAULT_DATE_ADMISSION.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())));
    }

    @Test
    @Transactional
    public void getPersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        // Get the personnel
        restPersonnelMockMvc.perform(get("/api/personnels/{id}", personnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(personnel.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.dateAdmission").value(DEFAULT_DATE_ADMISSION.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPersonnel() throws Exception {
        // Get the personnel
        restPersonnelMockMvc.perform(get("/api/personnels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);
        personnelSearchRepository.save(personnel);
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();

        // Update the personnel
        Personnel updatedPersonnel = new Personnel();
        updatedPersonnel.setId(personnel.getId());
        updatedPersonnel.setNom(UPDATED_NOM);
        updatedPersonnel.setPrenom(UPDATED_PRENOM);
        updatedPersonnel.setTel(UPDATED_TEL);
        updatedPersonnel.setEmail(UPDATED_EMAIL);
        updatedPersonnel.setDateAdmission(UPDATED_DATE_ADMISSION);
        updatedPersonnel.setIsActive(UPDATED_IS_ACTIVE);
        updatedPersonnel.setAdresse(UPDATED_ADRESSE);

        restPersonnelMockMvc.perform(put("/api/personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPersonnel)))
                .andExpect(status().isOk());

        // Validate the Personnel in the database
        List<Personnel> personnels = personnelRepository.findAll();
        assertThat(personnels).hasSize(databaseSizeBeforeUpdate);
        Personnel testPersonnel = personnels.get(personnels.size() - 1);
        assertThat(testPersonnel.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonnel.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonnel.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testPersonnel.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonnel.getDateAdmission()).isEqualTo(UPDATED_DATE_ADMISSION);
        assertThat(testPersonnel.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPersonnel.getAdresse()).isEqualTo(UPDATED_ADRESSE);

        // Validate the Personnel in ElasticSearch
        Personnel personnelEs = personnelSearchRepository.findOne(testPersonnel.getId());
        assertThat(personnelEs).isEqualToComparingFieldByField(testPersonnel);
    }

    @Test
    @Transactional
    public void deletePersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);
        personnelSearchRepository.save(personnel);
        int databaseSizeBeforeDelete = personnelRepository.findAll().size();

        // Get the personnel
        restPersonnelMockMvc.perform(delete("/api/personnels/{id}", personnel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean personnelExistsInEs = personnelSearchRepository.exists(personnel.getId());
        assertThat(personnelExistsInEs).isFalse();

        // Validate the database is empty
        List<Personnel> personnels = personnelRepository.findAll();
        assertThat(personnels).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);
        personnelSearchRepository.save(personnel);

        // Search the personnel
        restPersonnelMockMvc.perform(get("/api/_search/personnels?query=id:" + personnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].dateAdmission").value(hasItem(DEFAULT_DATE_ADMISSION.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())));
    }
}
