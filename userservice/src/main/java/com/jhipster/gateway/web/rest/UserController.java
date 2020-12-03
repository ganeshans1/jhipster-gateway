package com.jhipster.gateway.web.rest;

import com.jhipster.gateway.repository.*;
import com.jhipster.gateway.web.rest.errors.ResourceNotFoundException;
import javassist.bytecode.stackmap.BasicBlock;
import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    UserServiceRepository userServiceRepository;

    @Autowired
    UserCommentRepository userCommentRepository;

    @Autowired
    UserAddressRepository userAddressRepository;

    @PostMapping("/saveUserInfo")
    public UserInfo saveUserInfo(@Valid @RequestBody UserInfo userInfo){
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        userInfo.setModifiedAt(now);
        userInfo.setCreatedAt(now);
        Set<UserAddress> userAddresses = userInfo.getUserAddressSet();
        if(Objects.nonNull(userAddresses)){
            userAddresses.stream().forEach(address -> {
                address.setCreatedAt(now);
            });
        }
        Set<UserComments> userCommentsSet = userInfo.getUserCommentsSet();
        if(Objects.nonNull(userCommentsSet)){
            userCommentsSet.stream().forEach(comment -> {
                comment.setCreatedAt(now);
            });
        }
        return userServiceRepository.save(userInfo);
    }
    @PutMapping("/updateUserInfo/{userId}")
    public UserInfo updateUserInfo(@PathVariable Integer userId, @Valid @RequestBody UserInfo userInfo){
        return userServiceRepository.findById(userId).map(user->{
            user.setName(Objects.nonNull(userInfo.getName())?userInfo.getName():user.getName());
            user.setPhoneNumber(Objects.nonNull(userInfo.getPhoneNumber())?userInfo.getPhoneNumber():user.getPhoneNumber());
            user.setEmailId(Objects.nonNull(userInfo.getEmailId())?userInfo.getEmailId():user.getEmailId());
            user.setModifiedAt(LocalDateTime.now(ZoneId.systemDefault()));
            return userServiceRepository.save(user);
        }).orElseThrow(()->new ResourceNotFoundException("User id "+userId+" not found"));
    }

    @DeleteMapping("/deleteUserInfo/{userId}")
    public ResponseEntity<?> deleteUserInfo(@PathVariable Integer userId) {
        return userServiceRepository.findById(userId).map(user -> {
            userServiceRepository.delete(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @GetMapping("/getAllUserInfos")
    public List<UserInfo> userInfos(){
        return userServiceRepository.findAll();
    }

    @GetMapping("/getAllUserComments")
    public List<UserComments> getAllUserComments(){
        return userCommentRepository.findAll();
    }

    @PostMapping("/postUserComments/{userId}/comments")
    public UserComments postUserComments(@PathVariable Integer userId,@Valid @RequestBody UserComments userComments){
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return userServiceRepository.findById(userId).map(userInfo -> {
            userComments.setUserInfo(userInfo);
            userComments.setCreatedAt(now);
            return userCommentRepository.save(userComments);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @GetMapping("/getAllUserAddress")
    public List<UserAddress> getAllUserAddress(){
        return userAddressRepository.findAll();
    }

    @PostMapping("/postUserAddress/{userId}/address")
    public UserAddress postUserAddress(@PathVariable Integer userId,@Valid @RequestBody UserAddress userAddress){
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return userServiceRepository.findById(userId).map(userInfo -> {
            userAddress.setUserInfo(userInfo);
            userAddress.setCreatedAt(now);
            return userAddressRepository.save(userAddress);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

}
