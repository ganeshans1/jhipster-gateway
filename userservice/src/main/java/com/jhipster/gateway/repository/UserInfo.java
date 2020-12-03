package com.jhipster.gateway.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="UserInfo")
public class UserInfo {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userId;

    @Column(name="name")
    String name;

    @Column(name="email_id")
    String emailId;
    @Column(name="phone_number")
    int phoneNumber;
    @Column(name="created_at")
    LocalDateTime createdAt;

    @Column(name="modified_at")
    LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
     @JsonManagedReference
    Set<UserAddress> userAddressSet;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    Set<UserComments> userCommentsSet;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Set<UserAddress> getUserAddressSet() {
        return userAddressSet;
    }

    public void setUserAddressSet(Set<UserAddress> userAddressSet) {
        this.userAddressSet = userAddressSet;
    }

    public Set<UserComments> getUserCommentsSet() {
        return userCommentsSet;
    }

    public void setUserCommentsSet(Set<UserComments> userCommentsSet) {
        this.userCommentsSet = userCommentsSet;
    }

}
