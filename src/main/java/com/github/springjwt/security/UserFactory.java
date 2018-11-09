package com.github.springjwt.security;

import com.github.springjwt.domain.authority.Authority;
import com.github.springjwt.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public final class UserFactory {
    private UserFactory() {

    }

    public static JwtUser create(User user) {
        return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(),
                mapToGrantedAuthorities(user.getAuthorities()), user.getEnabled(), user.getLastPasswordResetDate());
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(a -> new SimpleGrantedAuthority(a.getName().name()))
                .collect(Collectors.toList());
    }
}
