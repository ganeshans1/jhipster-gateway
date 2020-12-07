package com.useraddress.com.domain;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient("usercomment")
public interface UserCommentApi {

    @GetMapping("/api/user-comments/user/{id}")
    Optional<Object> findByUserId(@PathVariable("id") Long id);
}
