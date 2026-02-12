package com.example.planaula.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    //private final Set<Integer> centrosPermitidos;

    //Set<Integer> centrosPermitidos
    public CustomUserDetails(Integer id, String username, String password,
                             List<SimpleGrantedAuthority> authorities2) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities2;
        //this.centrosPermitidos = centrosPermitidos;
    }

    public Integer getId() {
        return id;
    }

    /*public Set<Integer> getCentrosPermitidos() {
        return centrosPermitidos;
    }*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
