package com.github.springjwt.security.spring;

import com.github.springjwt.domain.user.User;
import com.github.springjwt.domain.user.UserRepository;
import com.github.springjwt.security.AuthoritiesConstants;
import com.github.springjwt.security.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class DaoUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (s.trim().isEmpty()){
            throw new IllegalArgumentException("No login email available");
        }

        String lowercaseEmail = s.toLowerCase();
        User userFromDatabase = userRepository.findByEmail(lowercaseEmail);

        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseEmail + " was not found in the database.");
        }

        return getPrincipalUser(userFromDatabase);
    }

    public static PrincipalUser getPrincipalUser(User user){
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
        grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));

        // TODO: Checked if the User is activated
        return new PrincipalUser(user.getEmail(), user.getPassword(), grantedAuthorities, user.getId());
    }

    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }
}