package com.userservice.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.userservice.app.domain.Userinfo;
import com.userservice.app.repository.UserinfoRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Userinfo.
 */
@RestController
@RequestMapping("/api")
public class UserinfoResource {

    private final Logger log = LoggerFactory.getLogger(UserinfoResource.class);

    private static final String ENTITY_NAME = "userinfo";

    private final UserinfoRepository userinfoRepository;

    public UserinfoResource(UserinfoRepository userinfoRepository) {
        this.userinfoRepository = userinfoRepository;
    }

    /**
     * POST  /userinfos : Create a new userinfo.
     *
     * @param userinfo the userinfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userinfo, or with status 400 (Bad Request) if the userinfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/userinfos")
    @Timed
    public ResponseEntity<Userinfo> createUserinfo(@Valid @RequestBody Userinfo userinfo) throws URISyntaxException {
        log.debug("REST request to save Userinfo : {}", userinfo);
        if (userinfo.getId() != null) {
            throw new BadRequestAlertException("A new userinfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Userinfo result = userinfoRepository.save(userinfo);
        return ResponseEntity.created(new URI("/api/userinfos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userinfos : Updates an existing userinfo.
     *
     * @param userinfo the userinfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userinfo,
     * or with status 400 (Bad Request) if the userinfo is not valid,
     * or with status 500 (Internal Server Error) if the userinfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/userinfos")
    @Timed
    public ResponseEntity<Userinfo> updateUserinfo(@Valid @RequestBody Userinfo userinfo) throws URISyntaxException {
        log.debug("REST request to update Userinfo : {}", userinfo);
        if (userinfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Userinfo result = userinfoRepository.save(userinfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userinfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userinfos : get all the userinfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userinfos in body
     */
    @GetMapping("/userinfos")
    @Timed
    public ResponseEntity<List<Userinfo>> getAllUserinfos(Pageable pageable) {
        log.debug("REST request to get a page of Userinfos");
        Page<Userinfo> page = userinfoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userinfos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userinfos/:id : get the "id" userinfo.
     *
     * @param id the id of the userinfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userinfo, or with status 404 (Not Found)
     */
    @GetMapping("/userinfos/{id}")
    @Timed
    public ResponseEntity<Userinfo> getUserinfo(@PathVariable Long id) {
        log.debug("REST request to get Userinfo : {}", id);
        Optional<Userinfo> userinfo = userinfoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userinfo);
    }

    /**
     * DELETE  /userinfos/:id : delete the "id" userinfo.
     *
     * @param id the id of the userinfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/userinfos/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserinfo(@PathVariable Long id) {
        log.debug("REST request to delete Userinfo : {}", id);

        userinfoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
