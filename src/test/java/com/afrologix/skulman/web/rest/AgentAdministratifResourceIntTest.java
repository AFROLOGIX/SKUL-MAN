package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.AgentAdministratif;
import com.afrologix.skulman.repository.AgentAdministratifRepository;
import com.afrologix.skulman.repository.search.AgentAdministratifSearchRepository;

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
 * Test class for the AgentAdministratifResource REST controller.
 *
 * @see AgentAdministratifResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class AgentAdministratifResourceIntTest {

    private static final String DEFAULT_ROLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private AgentAdministratifRepository agentAdministratifRepository;

    @Inject
    private AgentAdministratifSearchRepository agentAdministratifSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAgentAdministratifMockMvc;

    private AgentAdministratif agentAdministratif;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgentAdministratifResource agentAdministratifResource = new AgentAdministratifResource();
        ReflectionTestUtils.setField(agentAdministratifResource, "agentAdministratifSearchRepository", agentAdministratifSearchRepository);
        ReflectionTestUtils.setField(agentAdministratifResource, "agentAdministratifRepository", agentAdministratifRepository);
        this.restAgentAdministratifMockMvc = MockMvcBuilders.standaloneSetup(agentAdministratifResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        agentAdministratifSearchRepository.deleteAll();
        agentAdministratif = new AgentAdministratif();
        agentAdministratif.setRole(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createAgentAdministratif() throws Exception {
        int databaseSizeBeforeCreate = agentAdministratifRepository.findAll().size();

        // Create the AgentAdministratif

        restAgentAdministratifMockMvc.perform(post("/api/agent-administratifs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentAdministratif)))
                .andExpect(status().isCreated());

        // Validate the AgentAdministratif in the database
        List<AgentAdministratif> agentAdministratifs = agentAdministratifRepository.findAll();
        assertThat(agentAdministratifs).hasSize(databaseSizeBeforeCreate + 1);
        AgentAdministratif testAgentAdministratif = agentAdministratifs.get(agentAdministratifs.size() - 1);
        assertThat(testAgentAdministratif.getRole()).isEqualTo(DEFAULT_ROLE);

        // Validate the AgentAdministratif in ElasticSearch
        AgentAdministratif agentAdministratifEs = agentAdministratifSearchRepository.findOne(testAgentAdministratif.getId());
        assertThat(agentAdministratifEs).isEqualToComparingFieldByField(testAgentAdministratif);
    }

    @Test
    @Transactional
    public void getAllAgentAdministratifs() throws Exception {
        // Initialize the database
        agentAdministratifRepository.saveAndFlush(agentAdministratif);

        // Get all the agentAdministratifs
        restAgentAdministratifMockMvc.perform(get("/api/agent-administratifs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(agentAdministratif.getId().intValue())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @Test
    @Transactional
    public void getAgentAdministratif() throws Exception {
        // Initialize the database
        agentAdministratifRepository.saveAndFlush(agentAdministratif);

        // Get the agentAdministratif
        restAgentAdministratifMockMvc.perform(get("/api/agent-administratifs/{id}", agentAdministratif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(agentAdministratif.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAgentAdministratif() throws Exception {
        // Get the agentAdministratif
        restAgentAdministratifMockMvc.perform(get("/api/agent-administratifs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgentAdministratif() throws Exception {
        // Initialize the database
        agentAdministratifRepository.saveAndFlush(agentAdministratif);
        agentAdministratifSearchRepository.save(agentAdministratif);
        int databaseSizeBeforeUpdate = agentAdministratifRepository.findAll().size();

        // Update the agentAdministratif
        AgentAdministratif updatedAgentAdministratif = new AgentAdministratif();
        updatedAgentAdministratif.setId(agentAdministratif.getId());
        updatedAgentAdministratif.setRole(UPDATED_ROLE);

        restAgentAdministratifMockMvc.perform(put("/api/agent-administratifs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAgentAdministratif)))
                .andExpect(status().isOk());

        // Validate the AgentAdministratif in the database
        List<AgentAdministratif> agentAdministratifs = agentAdministratifRepository.findAll();
        assertThat(agentAdministratifs).hasSize(databaseSizeBeforeUpdate);
        AgentAdministratif testAgentAdministratif = agentAdministratifs.get(agentAdministratifs.size() - 1);
        assertThat(testAgentAdministratif.getRole()).isEqualTo(UPDATED_ROLE);

        // Validate the AgentAdministratif in ElasticSearch
        AgentAdministratif agentAdministratifEs = agentAdministratifSearchRepository.findOne(testAgentAdministratif.getId());
        assertThat(agentAdministratifEs).isEqualToComparingFieldByField(testAgentAdministratif);
    }

    @Test
    @Transactional
    public void deleteAgentAdministratif() throws Exception {
        // Initialize the database
        agentAdministratifRepository.saveAndFlush(agentAdministratif);
        agentAdministratifSearchRepository.save(agentAdministratif);
        int databaseSizeBeforeDelete = agentAdministratifRepository.findAll().size();

        // Get the agentAdministratif
        restAgentAdministratifMockMvc.perform(delete("/api/agent-administratifs/{id}", agentAdministratif.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean agentAdministratifExistsInEs = agentAdministratifSearchRepository.exists(agentAdministratif.getId());
        assertThat(agentAdministratifExistsInEs).isFalse();

        // Validate the database is empty
        List<AgentAdministratif> agentAdministratifs = agentAdministratifRepository.findAll();
        assertThat(agentAdministratifs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAgentAdministratif() throws Exception {
        // Initialize the database
        agentAdministratifRepository.saveAndFlush(agentAdministratif);
        agentAdministratifSearchRepository.save(agentAdministratif);

        // Search the agentAdministratif
        restAgentAdministratifMockMvc.perform(get("/api/_search/agent-administratifs?query=id:" + agentAdministratif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentAdministratif.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }
}
