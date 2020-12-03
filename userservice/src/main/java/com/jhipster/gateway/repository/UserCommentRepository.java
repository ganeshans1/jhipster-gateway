package com.jhipster.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCommentRepository extends JpaRepository<UserComments,Integer> {

}
