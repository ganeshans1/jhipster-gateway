package com.usercomment.domain;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient("useraddress")
public interface UserAddressApi {

    @GetMapping("/api/user-addresses/user/{id}")
    Optional<Object> findById(@PathVariable("id") Long id);
}
