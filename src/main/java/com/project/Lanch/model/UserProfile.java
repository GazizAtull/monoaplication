package com.project.Lanch.model;

import com.project.Lanch.builder.UserProfileBuilder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


/**
 * UserProfile represents additional profile information associated with a User.
 * - Fields include:
 *   - Unique ID (shared with the associated User).
 *   - Email, full name, phone, and address.
 * - Linked to a User through a one-to-one relationship with shared primary key (@MapsId).
 * - Can be constructed using a UserProfileBuilder for flexibility in setting fields.
 * - Uses JPA annotations for ORM mapping to the "user_profiles" table.
 */




@Entity
@Table(name = "user_profiles")
@Getter
@Setter
public class UserProfile {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String email;
    private String fullName;
    private String phone;
    private String address;


    public UserProfile() {}

    public UserProfile(UserProfileBuilder builder) {
        this.user = builder.user;
        this.email = builder.email;
        this.fullName = builder.fullName;
        this.phone = builder.phone;
        this.address = builder.address;
    }
}
