package com.jhipster.gateway.web.rest;

import com.jhipster.gateway.MyappApp;

import com.jhipster.gateway.domain.UserComments;
import com.jhipster.gateway.repository.UserCommentsRepository;
import com.jhipster.gateway.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.jhipster.gateway.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserCommentsResource REST controller.
 *
 * @see UserCommentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class UserCommentsResourceIntTest {

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private UserCommentsRepository userCommentsRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserCommentsMockMvc;

    private UserComments userComments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserCommentsResource userCommentsResource = new UserCommentsResource(userCommentsRepository);
        this.restUserCommentsMockMvc = MockMvcBuilders.standaloneSetup(userCommentsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserComments createEntity(EntityManager em) {
        UserComments userComments = new UserComments()
            .userId(DEFAULT_USER_ID)
            .comment(DEFAULT_COMMENT)
            .createdAt(DEFAULT_CREATED_AT);
        return userComments;
    }

    @Before
    public void initTest() {
        userComments = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserComments() throws Exception {
        int databaseSizeBeforeCreate = userCommentsRepository.findAll().size();

        // Create the UserComments
        restUserCommentsMockMvc.perform(post("/api/user-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userComments)))
            .andExpect(status().isCreated());

        // Validate the UserComments in the database
        List<UserComments> userCommentsList = userCommentsRepository.findAll();
        assertThat(userCommentsList).hasSize(databaseSizeBeforeCreate + 1);
        UserComments testUserComments = userCommentsList.get(userCommentsList.size() - 1);
        assertThat(testUserComments.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserComments.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testUserComments.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createUserCommentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userCommentsRepository.findAll().size();

        // Create the UserComments with an existing ID
        userComments.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCommentsMockMvc.perform(post("/api/user-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userComments)))
            .andExpect(status().isBadRequest());

        // Validate the UserComments in the database
        List<UserComments> userCommentsList = userCommentsRepository.findAll();
        assertThat(userCommentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserComments() throws Exception {
        // Initialize the database
        userCommentsRepository.saveAndFlush(userComments);

        // Get all the userCommentsList
        restUserCommentsMockMvc.perform(get("/api/user-comments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userComments.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getUserComments() throws Exception {
        // Initialize the database
        userCommentsRepository.saveAndFlush(userComments);

        // Get the userComments
        restUserCommentsMockMvc.perform(get("/api/user-comments/{id}", userComments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userComments.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingUserComments() throws Exception {
        // Get the userComments
        restUserCommentsMockMvc.perform(get("/api/user-comments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserComments() throws Exception {
        // Initialize the database
        userCommentsRepository.saveAndFlush(userComments);

        int databaseSizeBeforeUpdate = userCommentsRepository.findAll().size();

        // Update the userComments
        UserComments updatedUserComments = userCommentsRepository.findById(userComments.getId()).get();
        // Disconnect from session so that the updates on updatedUserComments are not directly saved in db
        em.detach(updatedUserComments);
        updatedUserComments
            .userId(UPDATED_USER_ID)
            .comment(UPDATED_COMMENT)
            .createdAt(UPDATED_CREATED_AT);

        restUserCommentsMockMvc.perform(put("/api/user-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserComments)))
            .andExpect(status().isOk());

        // Validate the UserComments in the database
        List<UserComments> userCommentsList = userCommentsRepository.findAll();
        assertThat(userCommentsList).hasSize(databaseSizeBeforeUpdate);
        UserComments testUserComments = userCommentsList.get(userCommentsList.size() - 1);
        assertThat(testUserComments.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserComments.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testUserComments.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingUserComments() throws Exception {
        int databaseSizeBeforeUpdate = userCommentsRepository.findAll().size();

        // Create the UserComments

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restUserCommentsMockMvc.perform(put("/api/user-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userComments)))
            .andExpect(status().isBadRequest());

        // Validate the UserComments in the database
        List<UserComments> userCommentsList = userCommentsRepository.findAll();
        assertThat(userCommentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserComments() throws Exception {
        // Initialize the database
        userCommentsRepository.saveAndFlush(userComments);

        int databaseSizeBeforeDelete = userCommentsRepository.findAll().size();

        // Get the userComments
        restUserCommentsMockMvc.perform(delete("/api/user-comments/{id}", userComments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserComments> userCommentsList = userCommentsRepository.findAll();
        assertThat(userCommentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserComments.class);
        UserComments userComments1 = new UserComments();
        userComments1.setId(1L);
        UserComments userComments2 = new UserComments();
        userComments2.setId(userComments1.getId());
        assertThat(userComments1).isEqualTo(userComments2);
        userComments2.setId(2L);
        assertThat(userComments1).isNotEqualTo(userComments2);
        userComments1.setId(null);
        assertThat(userComments1).isNotEqualTo(userComments2);
    }
}
