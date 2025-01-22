package com.project.Lanch.builder;

import com.project.Lanch.model.User;
import com.project.Lanch.model.UserProfile;



/**
 * The UserProfileBuilder class is used for step-by-step construction of a UserProfile object.
 */

public class UserProfileBuilder {
    public User user;
    public String email;
    public String fullName;
    public String phone;
    public String address;

    public UserProfileBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public UserProfileBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserProfileBuilder setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UserProfileBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserProfileBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public UserProfile build() {
        return new UserProfile(this);
    }


}
