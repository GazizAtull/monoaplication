package com.project.Lanch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


/**
 * User represents a system user with authentication and authorization details.
 * - Fields include:
 *   - Unique ID.
 *   - Username (must be unique and not null).
 *   - Password (not null).
 *   - Account status (enabled by default).
 *   - Roles for authorization (stored as a set of strings).
 * - Associated with a UserProfile through a one-to-one relationship.
 * - Uses JPA annotations for ORM mapping to the "users" table.
 */




@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority")
    private Set<String> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    public User() {}

    public User(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

}
