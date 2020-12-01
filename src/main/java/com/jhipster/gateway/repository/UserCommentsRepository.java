package com.jhipster.gateway.repository;

import com.jhipster.gateway.domain.UserComments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserComments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserCommentsRepository extends JpaRepository<UserComments, Long> {

}
