package com.bakend.postoak.infraestructure.security;

import com.bakend.postoak.infraestructure.persistence.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class UserDetailsImpl implements UserDetails {

    private final UUID id;          // Cambiado de Long a UUID según el ERD
    private final String email;
    private final String password;
    private final String fullName;  // Cambiado de nombre a fullName

    public UserDetailsImpl(UserEntity usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.password = usuario.getPasswordHash(); // Cambiado a getPasswordHash()
        this.fullName = usuario.getFullName();     // Cambiado a getFullName()
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public UUID getId() {           // Devuelve UUID
        return id;
    }

    public String getFullName() {   // Cambiado a getFullName()
        return fullName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}