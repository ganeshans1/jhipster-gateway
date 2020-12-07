package com.usercomment.domain;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient("gateway")
public interface UserApi {
    @GetMapping("/api/users/{id}")
    Optional<Object> findById(@PathVariable("id") Long id);
}
