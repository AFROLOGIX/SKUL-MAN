package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.LoginConnexion;
import com.afrologix.skulman.repository.LoginConnexionRepository;
import com.afrologix.skulman.repository.search.LoginConnexionSearchRepository;

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
 * Test class for the LoginConnexionResource REST controller.
 *
 * @see LoginConnexionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class LoginConnexionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_ROLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LOGIN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LOGIN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LOGIN_TIME_STR = dateTimeFormatter.format(DEFAULT_LOGIN_TIME);

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;
    private static final String DEFAULT_ADDRESS_IP = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ADDRESS_IP = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_NB_ECHECS = 10000;
    private static final Integer UPDATED_NB_ECHECS = 9999;

    private static final ZonedDateTime DEFAULT_DATE_ECHEC = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_ECHEC = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_ECHEC_STR = dateTimeFormatter.format(DEFAULT_DATE_ECHEC);

    @Inject
    private LoginConnexionRepository loginConnexionRepository;

    @Inject
    private LoginConnexionSearchRepository loginConnexionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLoginConnexionMockMvc;

    private LoginConnexion loginConnexion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LoginConnexionResource loginConnexionResource = new LoginConnexionResource();
        ReflectionTestUtils.setField(loginConnexionResource, "loginConnexionSearchRepository", loginConnexionSearchRepository);
        ReflectionTestUtils.setField(loginConnexionResource, "loginConnexionRepository", loginConnexionRepository);
        this.restLoginConnexionMockMvc = MockMvcBuilders.standaloneSetup(loginConnexionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        loginConnexionSearchRepository.deleteAll();
        loginConnexion = new LoginConnexion();
        loginConnexion.setRole(DEFAULT_ROLE);
        loginConnexion.setLoginTime(DEFAULT_LOGIN_TIME);
        loginConnexion.setStatus(DEFAULT_STATUS);
        loginConnexion.setAddressIp(DEFAULT_ADDRESS_IP);
        loginConnexion.setNbEchecs(DEFAULT_NB_ECHECS);
        loginConnexion.setDateEchec(DEFAULT_DATE_ECHEC);
    }

    @Test
    @Transactional
    public void createLoginConnexion() throws Exception {
        int databaseSizeBeforeCreate = loginConnexionRepository.findAll().size();

        // Create the LoginConnexion

        restLoginConnexionMockMvc.perform(post("/api/login-connexions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loginConnexion)))
                .andExpect(status().isCreated());

        // Validate the LoginConnexion in the database
        List<LoginConnexion> loginConnexions = loginConnexionRepository.findAll();
        assertThat(loginConnexions).hasSize(databaseSizeBeforeCreate + 1);
        LoginConnexion testLoginConnexion = loginConnexions.get(loginConnexions.size() - 1);
        assertThat(testLoginConnexion.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testLoginConnexion.getLoginTime()).isEqualTo(DEFAULT_LOGIN_TIME);
        assertThat(testLoginConnexion.isStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLoginConnexion.getAddressIp()).isEqualTo(DEFAULT_ADDRESS_IP);
        assertThat(testLoginConnexion.getNbEchecs()).isEqualTo(DEFAULT_NB_ECHECS);
        assertThat(testLoginConnexion.getDateEchec()).isEqualTo(DEFAULT_DATE_ECHEC);

        // Validate the LoginConnexion in ElasticSearch
        LoginConnexion loginConnexionEs = loginConnexionSearchRepository.findOne(testLoginConnexion.getId());
        assertThat(loginConnexionEs).isEqualToComparingFieldByField(testLoginConnexion);
    }

    @Test
    @Transactional
    public void getAllLoginConnexions() throws Exception {
        // Initialize the database
        loginConnexionRepository.saveAndFlush(loginConnexion);

        // Get all the loginConnexions
        restLoginConnexionMockMvc.perform(get("/api/login-connexions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(loginConnexion.getId().intValue())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
                .andExpect(jsonPath("$.[*].loginTime").value(hasItem(DEFAULT_LOGIN_TIME_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
                .andExpect(jsonPath("$.[*].addressIp").value(hasItem(DEFAULT_ADDRESS_IP.toString())))
                .andExpect(jsonPath("$.[*].nbEchecs").value(hasItem(DEFAULT_NB_ECHECS)))
                .andExpect(jsonPath("$.[*].dateEchec").value(hasItem(DEFAULT_DATE_ECHEC_STR)));
    }

    @Test
    @Transactional
    public void getLoginConnexion() throws Exception {
        // Initialize the database
        loginConnexionRepository.saveAndFlush(loginConnexion);

        // Get the loginConnexion
        restLoginConnexionMockMvc.perform(get("/api/login-connexions/{id}", loginConnexion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(loginConnexion.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.loginTime").value(DEFAULT_LOGIN_TIME_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.addressIp").value(DEFAULT_ADDRESS_IP.toString()))
            .andExpect(jsonPath("$.nbEchecs").value(DEFAULT_NB_ECHECS))
            .andExpect(jsonPath("$.dateEchec").value(DEFAULT_DATE_ECHEC_STR));
    }

    @Test
    @Transactional
    public void getNonExistingLoginConnexion() throws Exception {
        // Get the loginConnexion
        restLoginConnexionMockMvc.perform(get("/api/login-connexions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoginConnexion() throws Exception {
        // Initialize the database
        loginConnexionRepository.saveAndFlush(loginConnexion);
        loginConnexionSearchRepository.save(loginConnexion);
        int databaseSizeBeforeUpdate = loginConnexionRepository.findAll().size();

        // Update the loginConnexion
        LoginConnexion updatedLoginConnexion = new LoginConnexion();
        updatedLoginConnexion.setId(loginConnexion.getId());
        updatedLoginConnexion.setRole(UPDATED_ROLE);
        updatedLoginConnexion.setLoginTime(UPDATED_LOGIN_TIME);
        updatedLoginConnexion.setStatus(UPDATED_STATUS);
        updatedLoginConnexion.setAddressIp(UPDATED_ADDRESS_IP);
        updatedLoginConnexion.setNbEchecs(UPDATED_NB_ECHECS);
        updatedLoginConnexion.setDateEchec(UPDATED_DATE_ECHEC);

        restLoginConnexionMockMvc.perform(put("/api/login-connexions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLoginConnexion)))
                .andExpect(status().isOk());

        // Validate the LoginConnexion in the database
        List<LoginConnexion> loginConnexions = loginConnexionRepository.findAll();
        assertThat(loginConnexions).hasSize(databaseSizeBeforeUpdate);
        LoginConnexion testLoginConnexion = loginConnexions.get(loginConnexions.size() - 1);
        assertThat(testLoginConnexion.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testLoginConnexion.getLoginTime()).isEqualTo(UPDATED_LOGIN_TIME);
        assertThat(testLoginConnexion.isStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLoginConnexion.getAddressIp()).isEqualTo(UPDATED_ADDRESS_IP);
        assertThat(testLoginConnexion.getNbEchecs()).isEqualTo(UPDATED_NB_ECHECS);
        assertThat(testLoginConnexion.getDateEchec()).isEqualTo(UPDATED_DATE_ECHEC);

        // Validate the LoginConnexion in ElasticSearch
        LoginConnexion loginConnexionEs = loginConnexionSearchRepository.findOne(testLoginConnexion.getId());
        assertThat(loginConnexionEs).isEqualToComparingFieldByField(testLoginConnexion);
    }

    @Test
    @Transactional
    public void deleteLoginConnexion() throws Exception {
        // Initialize the database
        loginConnexionRepository.saveAndFlush(loginConnexion);
        loginConnexionSearchRepository.save(loginConnexion);
        int databaseSizeBeforeDelete = loginConnexionRepository.findAll().size();

        // Get the loginConnexion
        restLoginConnexionMockMvc.perform(delete("/api/login-connexions/{id}", loginConnexion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean loginConnexionExistsInEs = loginConnexionSearchRepository.exists(loginConnexion.getId());
        assertThat(loginConnexionExistsInEs).isFalse();

        // Validate the database is empty
        List<LoginConnexion> loginConnexions = loginConnexionRepository.findAll();
        assertThat(loginConnexions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLoginConnexion() throws Exception {
        // Initialize the database
        loginConnexionRepository.saveAndFlush(loginConnexion);
        loginConnexionSearchRepository.save(loginConnexion);

        // Search the loginConnexion
        restLoginConnexionMockMvc.perform(get("/api/_search/login-connexions?query=id:" + loginConnexion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginConnexion.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].loginTime").value(hasItem(DEFAULT_LOGIN_TIME_STR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].addressIp").value(hasItem(DEFAULT_ADDRESS_IP.toString())))
            .andExpect(jsonPath("$.[*].nbEchecs").value(hasItem(DEFAULT_NB_ECHECS)))
            .andExpect(jsonPath("$.[*].dateEchec").value(hasItem(DEFAULT_DATE_ECHEC_STR)));
    }
}
