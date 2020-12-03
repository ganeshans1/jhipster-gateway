package com.userservice.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.userservice.app.domain.Usercomment;
import com.userservice.app.repository.UsercommentRepository;
import com.userservice.app.web.rest.errors.BadRequestAlertException;
import com.userservice.app.web.rest.util.HeaderUtil;
import com.userservice.app.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Usercomment.
 */
@RestController
@RequestMapping("/api")
public class UsercommentResource {

    private final Logger log = LoggerFactory.getLogger(UsercommentResource.class);

    private static final String ENTITY_NAME = "usercomment";

    private final UsercommentRepository usercommentRepository;

    public UsercommentResource(UsercommentRepository usercommentRepository) {
        this.usercommentRepository = usercommentRepository;
    }

    /**
     * POST  /usercomments : Create a new usercomment.
     *
     * @param usercomment the usercomment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new usercomment, or with status 400 (Bad Request) if the usercomment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/usercomments")
    @Timed
    public ResponseEntity<Usercomment> createUsercomment(@RequestBody Usercomment usercomment) throws URISyntaxException {
        log.debug("REST request to save Usercomment : {}", usercomment);
        if (usercomment.getId() != null) {
            throw new BadRequestAlertException("A new usercomment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Usercomment result = usercommentRepository.save(usercomment);
        return ResponseEntity.created(new URI("/api/usercomments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /usercomments : Updates an existing usercomment.
     *
     * @param usercomment the usercomment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated usercomment,
     * or with status 400 (Bad Request) if the usercomment is not valid,
     * or with status 500 (Internal Server Error) if the usercomment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/usercomments")
    @Timed
    public ResponseEntity<Usercomment> updateUsercomment(@RequestBody Usercomment usercomment) throws URISyntaxException {
        log.debug("REST request to update Usercomment : {}", usercomment);
        if (usercomment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Usercomment result = usercommentRepository.save(usercomment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, usercomment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /usercomments : get all the usercomments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of usercomments in body
     */
    @GetMapping("/usercomments")
    @Timed
    public ResponseEntity<List<Usercomment>> getAllUsercomments(Pageable pageable) {
        log.debug("REST request to get a page of Usercomments");
        Page<Usercomment> page = usercommentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/usercomments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /usercomments/:id : get the "id" usercomment.
     *
     * @param id the id of the usercomment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the usercomment, or with status 404 (Not Found)
     */
    @GetMapping("/usercomments/{id}")
    @Timed
    public ResponseEntity<Usercomment> getUsercomment(@PathVariable Long id) {
        log.debug("REST request to get Usercomment : {}", id);
        Optional<Usercomment> usercomment = usercommentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(usercomment);
    }

    /**
     * DELETE  /usercomments/:id : delete the "id" usercomment.
     *
     * @param id the id of the usercomment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/usercomments/{id}")
    @Timed
    public ResponseEntity<Void> deleteUsercomment(@PathVariable Long id) {
        log.debug("REST request to delete Usercomment : {}", id);

        usercommentRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
