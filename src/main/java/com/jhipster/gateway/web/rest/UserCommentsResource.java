package com.jhipster.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.gateway.domain.UserComments;
import com.jhipster.gateway.repository.UserCommentsRepository;
import com.jhipster.gateway.web.rest.errors.BadRequestAlertException;
import com.jhipster.gateway.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserComments.
 */
@RestController
@RequestMapping("/api")
public class UserCommentsResource {

    private final Logger log = LoggerFactory.getLogger(UserCommentsResource.class);

    private static final String ENTITY_NAME = "userComments";

    private final UserCommentsRepository userCommentsRepository;

    public UserCommentsResource(UserCommentsRepository userCommentsRepository) {
        this.userCommentsRepository = userCommentsRepository;
    }

    /**
     * POST  /user-comments : Create a new userComments.
     *
     * @param userComments the userComments to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userComments, or with status 400 (Bad Request) if the userComments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-comments")
    @Timed
    public ResponseEntity<UserComments> createUserComments(@RequestBody UserComments userComments) throws URISyntaxException {
        log.debug("REST request to save UserComments : {}", userComments);
        if (userComments.getId() != null) {
            throw new BadRequestAlertException("A new userComments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserComments result = userCommentsRepository.save(userComments);
        return ResponseEntity.created(new URI("/api/user-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-comments : Updates an existing userComments.
     *
     * @param userComments the userComments to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userComments,
     * or with status 400 (Bad Request) if the userComments is not valid,
     * or with status 500 (Internal Server Error) if the userComments couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-comments")
    @Timed
    public ResponseEntity<UserComments> updateUserComments(@RequestBody UserComments userComments) throws URISyntaxException {
        log.debug("REST request to update UserComments : {}", userComments);
        if (userComments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserComments result = userCommentsRepository.save(userComments);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userComments.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-comments : get all the userComments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userComments in body
     */
    @GetMapping("/user-comments")
    @Timed
    public List<UserComments> getAllUserComments() {
        log.debug("REST request to get all UserComments");
        return userCommentsRepository.findAll();
    }

    /**
     * GET  /user-comments/:id : get the "id" userComments.
     *
     * @param id the id of the userComments to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userComments, or with status 404 (Not Found)
     */
    @GetMapping("/user-comments/{id}")
    @Timed
    public ResponseEntity<UserComments> getUserComments(@PathVariable Long id) {
        log.debug("REST request to get UserComments : {}", id);
        Optional<UserComments> userComments = userCommentsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userComments);
    }

    /**
     * DELETE  /user-comments/:id : delete the "id" userComments.
     *
     * @param id the id of the userComments to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-comments/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserComments(@PathVariable Long id) {
        log.debug("REST request to delete UserComments : {}", id);

        userCommentsRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
