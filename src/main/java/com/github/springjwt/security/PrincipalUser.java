package com.github.springjwt.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

public class PrincipalUser extends User {
    private UUID id;
    private String salt;

    public PrincipalUser(String username, String password, String salt, Collection<? extends GrantedAuthority> authorities,
                         UUID id){
        super(username, password, authorities);
        this.id = id;
        this.salt = salt;
    }

    public PrincipalUser(String username, String password, String salt, Collection<? extends GrantedAuthority> authorities,
                         UUID id, boolean enabled, boolean accountNonExpired, boolean credentailsNonExpired,
                         boolean accountNonLocked){
        super(username, password, enabled, accountNonExpired, credentailsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.salt = salt;
    }

    public UUID getId(){
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
