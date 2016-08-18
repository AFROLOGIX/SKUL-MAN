package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.LoginAction;
import com.afrologix.skulman.repository.LoginActionRepository;
import com.afrologix.skulman.repository.search.LoginActionSearchRepository;

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
 * Test class for the LoginActionResource REST controller.
 *
 * @see LoginActionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class LoginActionResourceIntTest {

    private static final String DEFAULT_ACTION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ROLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ADRESSE_IP = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ADRESSE_IP = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private LoginActionRepository loginActionRepository;

    @Inject
    private LoginActionSearchRepository loginActionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLoginActionMockMvc;

    private LoginAction loginAction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LoginActionResource loginActionResource = new LoginActionResource();
        ReflectionTestUtils.setField(loginActionResource, "loginActionSearchRepository", loginActionSearchRepository);
        ReflectionTestUtils.setField(loginActionResource, "loginActionRepository", loginActionRepository);
        this.restLoginActionMockMvc = MockMvcBuilders.standaloneSetup(loginActionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        loginActionSearchRepository.deleteAll();
        loginAction = new LoginAction();
        loginAction.setAction(DEFAULT_ACTION);
        loginAction.setCreateBy(DEFAULT_CREATE_BY);
        loginAction.setRole(DEFAULT_ROLE);
        loginAction.setAdresseIp(DEFAULT_ADRESSE_IP);
    }

    @Test
    @Transactional
    public void createLoginAction() throws Exception {
        int databaseSizeBeforeCreate = loginActionRepository.findAll().size();

        // Create the LoginAction

        restLoginActionMockMvc.perform(post("/api/login-actions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loginAction)))
                .andExpect(status().isCreated());

        // Validate the LoginAction in the database
        List<LoginAction> loginActions = loginActionRepository.findAll();
        assertThat(loginActions).hasSize(databaseSizeBeforeCreate + 1);
        LoginAction testLoginAction = loginActions.get(loginActions.size() - 1);
        assertThat(testLoginAction.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testLoginAction.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testLoginAction.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testLoginAction.getAdresseIp()).isEqualTo(DEFAULT_ADRESSE_IP);

        // Validate the LoginAction in ElasticSearch
        LoginAction loginActionEs = loginActionSearchRepository.findOne(testLoginAction.getId());
        assertThat(loginActionEs).isEqualToComparingFieldByField(testLoginAction);
    }

    @Test
    @Transactional
    public void getAllLoginActions() throws Exception {
        // Initialize the database
        loginActionRepository.saveAndFlush(loginAction);

        // Get all the loginActions
        restLoginActionMockMvc.perform(get("/api/login-actions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(loginAction.getId().intValue())))
                .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
                .andExpect(jsonPath("$.[*].adresseIp").value(hasItem(DEFAULT_ADRESSE_IP.toString())));
    }

    @Test
    @Transactional
    public void getLoginAction() throws Exception {
        // Initialize the database
        loginActionRepository.saveAndFlush(loginAction);

        // Get the loginAction
        restLoginActionMockMvc.perform(get("/api/login-actions/{id}", loginAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(loginAction.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.adresseIp").value(DEFAULT_ADRESSE_IP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLoginAction() throws Exception {
        // Get the loginAction
        restLoginActionMockMvc.perform(get("/api/login-actions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoginAction() throws Exception {
        // Initialize the database
        loginActionRepository.saveAndFlush(loginAction);
        loginActionSearchRepository.save(loginAction);
        int databaseSizeBeforeUpdate = loginActionRepository.findAll().size();

        // Update the loginAction
        LoginAction updatedLoginAction = new LoginAction();
        updatedLoginAction.setId(loginAction.getId());
        updatedLoginAction.setAction(UPDATED_ACTION);
        updatedLoginAction.setCreateBy(UPDATED_CREATE_BY);
        updatedLoginAction.setRole(UPDATED_ROLE);
        updatedLoginAction.setAdresseIp(UPDATED_ADRESSE_IP);

        restLoginActionMockMvc.perform(put("/api/login-actions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLoginAction)))
                .andExpect(status().isOk());

        // Validate the LoginAction in the database
        List<LoginAction> loginActions = loginActionRepository.findAll();
        assertThat(loginActions).hasSize(databaseSizeBeforeUpdate);
        LoginAction testLoginAction = loginActions.get(loginActions.size() - 1);
        assertThat(testLoginAction.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testLoginAction.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testLoginAction.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testLoginAction.getAdresseIp()).isEqualTo(UPDATED_ADRESSE_IP);

        // Validate the LoginAction in ElasticSearch
        LoginAction loginActionEs = loginActionSearchRepository.findOne(testLoginAction.getId());
        assertThat(loginActionEs).isEqualToComparingFieldByField(testLoginAction);
    }

    @Test
    @Transactional
    public void deleteLoginAction() throws Exception {
        // Initialize the database
        loginActionRepository.saveAndFlush(loginAction);
        loginActionSearchRepository.save(loginAction);
        int databaseSizeBeforeDelete = loginActionRepository.findAll().size();

        // Get the loginAction
        restLoginActionMockMvc.perform(delete("/api/login-actions/{id}", loginAction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean loginActionExistsInEs = loginActionSearchRepository.exists(loginAction.getId());
        assertThat(loginActionExistsInEs).isFalse();

        // Validate the database is empty
        List<LoginAction> loginActions = loginActionRepository.findAll();
        assertThat(loginActions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLoginAction() throws Exception {
        // Initialize the database
        loginActionRepository.saveAndFlush(loginAction);
        loginActionSearchRepository.save(loginAction);

        // Search the loginAction
        restLoginActionMockMvc.perform(get("/api/_search/login-actions?query=id:" + loginAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].adresseIp").value(hasItem(DEFAULT_ADRESSE_IP.toString())));
    }
}
