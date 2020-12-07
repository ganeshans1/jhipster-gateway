package com.usercomment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.usercomment.domain.UserAddressApi;
import com.usercomment.domain.UserApi;
import com.usercomment.domain.UserComment;
import com.usercomment.domain.UserDetail;
import com.usercomment.repository.UserCommentRepository;
import com.usercomment.web.rest.errors.BadRequestAlertException;
import com.usercomment.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing UserComment.
 */
@RestController
@RequestMapping("/api")
public class UserCommentResource {

    private final Logger log = LoggerFactory.getLogger(UserCommentResource.class);

    private static final String ENTITY_NAME = "userComment";

    private final UserCommentRepository userCommentRepository;

    private final UserApi userApi;

    private final UserAddressApi userAddressApi;

    public UserCommentResource(UserCommentRepository userCommentRepository, UserApi userApi, UserAddressApi userAddressApi) {
        this.userCommentRepository = userCommentRepository;
        this.userApi = userApi;
        this.userAddressApi = userAddressApi;
    }

    /**
     * POST  /user-comments : Create a new userComment.
     *
     * @param userComment the userComment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userComment, or with status 400 (Bad Request) if the userComment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-comments")
    @Timed
    public ResponseEntity<UserComment> createUserComment(@RequestBody UserComment userComment) throws URISyntaxException {
        log.debug("REST request to save UserComment : {}", userComment);
        if (userComment.getId() != null) {
            throw new BadRequestAlertException("A new userComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserComment result = userCommentRepository.save(userComment);
        return ResponseEntity.created(new URI("/api/user-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-comments : Updates an existing userComment.
     *
     * @param userComment the userComment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userComment,
     * or with status 400 (Bad Request) if the userComment is not valid,
     * or with status 500 (Internal Server Error) if the userComment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-comments")
    @Timed
    public ResponseEntity<UserComment> updateUserComment(@RequestBody UserComment userComment) throws URISyntaxException {
        log.debug("REST request to update UserComment : {}", userComment);
        if (userComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserComment result = userCommentRepository.save(userComment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userComment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-comments : get all the userComments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userComments in body
     */
    @GetMapping("/user-comments")
    @Timed
    public List<UserComment> getAllUserComments() {
        log.debug("REST request to get all UserComments");
        return userCommentRepository.findAll();
    }

    /**
     * GET  /user-comments/:id : get the "id" userComment.
     *
     * @param id the id of the userComment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userComment, or with status 404 (Not Found)
     */
    @GetMapping("/user-comments/{id}")
    @Timed
    public Map<String, Object> getUserComment(@PathVariable Long id) {
        log.debug("REST request to get UserComment : {}", id);
        Map<String,Object> responseMap = new HashMap<>();
        Optional<UserComment> userComment = userCommentRepository.findById(id);
        if(userComment.isPresent()){
            responseMap.put("user",userApi.findById(userComment.get().getUserId()).get());
            responseMap.put("userAddress",userAddressApi.findById(userComment.get().getUserId()).get());
            responseMap.put("userComment",userComment.get());
        }
        return responseMap;
    }

    /**
     * GET  /user-comments/:id : get the "id" userComment.
     *
     * @param id the id of the userComment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userComment, or with status 404 (Not Found)
     */
    @GetMapping("/user-comments/user/{id}")
    @Timed
    public ResponseEntity<UserComment> getCommentByUserId(@PathVariable Long id) {
        log.debug("REST request to get UserComment : {}", id);
        Optional<UserComment> userComment = userCommentRepository.findByUserId(id);
        return ResponseUtil.wrapOrNotFound(userComment);
    }

    /**
     * DELETE  /user-comments/:id : delete the "id" userComment.
     *
     * @param id the id of the userComment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-comments/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserComment(@PathVariable Long id) {
        log.debug("REST request to delete UserComment : {}", id);

        userCommentRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
