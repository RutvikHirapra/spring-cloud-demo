package com.auth.userauthservice.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User implements Serializable {
    private static final long serialVersionUID = 65981149772133526L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "PROVIDER_USER_ID")
    private String providerUserId;

    private String email;

    @Column(name = "enabled", columnDefinition = "BIT", length = 1)
    private boolean enabled;

    private String imageUrl;
    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "created_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name = "USING_2FA")
    private boolean using2FA;

    private String secret;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "USER_ID")}, inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    private Set<Role> roles;
}
