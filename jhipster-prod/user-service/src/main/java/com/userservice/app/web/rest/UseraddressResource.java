package com.userservice.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.userservice.app.domain.Useraddress;
import com.userservice.app.repository.UseraddressRepository;
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
 * REST controller for managing Useraddress.
 */
@RestController
@RequestMapping("/api")
public class UseraddressResource {

    private final Logger log = LoggerFactory.getLogger(UseraddressResource.class);

    private static final String ENTITY_NAME = "useraddress";

    private final UseraddressRepository useraddressRepository;

    public UseraddressResource(UseraddressRepository useraddressRepository) {
        this.useraddressRepository = useraddressRepository;
    }

    /**
     * POST  /useraddresses : Create a new useraddress.
     *
     * @param useraddress the useraddress to create
     * @return the ResponseEntity with status 201 (Created) and with body the new useraddress, or with status 400 (Bad Request) if the useraddress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/useraddresses")
    @Timed
    public ResponseEntity<Useraddress> createUseraddress(@RequestBody Useraddress useraddress) throws URISyntaxException {
        log.debug("REST request to save Useraddress : {}", useraddress);
        if (useraddress.getId() != null) {
            throw new BadRequestAlertException("A new useraddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Useraddress result = useraddressRepository.save(useraddress);
        return ResponseEntity.created(new URI("/api/useraddresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /useraddresses : Updates an existing useraddress.
     *
     * @param useraddress the useraddress to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated useraddress,
     * or with status 400 (Bad Request) if the useraddress is not valid,
     * or with status 500 (Internal Server Error) if the useraddress couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/useraddresses")
    @Timed
    public ResponseEntity<Useraddress> updateUseraddress(@RequestBody Useraddress useraddress) throws URISyntaxException {
        log.debug("REST request to update Useraddress : {}", useraddress);
        if (useraddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Useraddress result = useraddressRepository.save(useraddress);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, useraddress.getId().toString()))
            .body(result);
    }

    /**
     * GET  /useraddresses : get all the useraddresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of useraddresses in body
     */
    @GetMapping("/useraddresses")
    @Timed
    public ResponseEntity<List<Useraddress>> getAllUseraddresses(Pageable pageable) {
        log.debug("REST request to get a page of Useraddresses");
        Page<Useraddress> page = useraddressRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/useraddresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /useraddresses/:id : get the "id" useraddress.
     *
     * @param id the id of the useraddress to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the useraddress, or with status 404 (Not Found)
     */
    @GetMapping("/useraddresses/{id}")
    @Timed
    public ResponseEntity<Useraddress> getUseraddress(@PathVariable Long id) {
        log.debug("REST request to get Useraddress : {}", id);
        Optional<Useraddress> useraddress = useraddressRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(useraddress);
    }

    /**
     * DELETE  /useraddresses/:id : delete the "id" useraddress.
     *
     * @param id the id of the useraddress to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/useraddresses/{id}")
    @Timed
    public ResponseEntity<Void> deleteUseraddress(@PathVariable Long id) {
        log.debug("REST request to delete Useraddress : {}", id);

        useraddressRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
