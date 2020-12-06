package com.usercomment.domain;

public class UserDetail {
    private User user;
    private UserAddress userAddress;
    private UserComment userComment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public UserComment getUserComment() {
        return userComment;
    }

    public void setUserComment(UserComment userComment) {
        this.userComment = userComment;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
            "user=" + user +
            ", userAddress=" + userAddress +
            ", userComment=" + userComment +
            '}';
    }

}
