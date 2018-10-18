package com.github.springjwt;

import com.github.springjwt.security.jwt.filter.TokenAuthenticationFilter;
import com.github.springjwt.security.spring.Http401UnauthorizedEntryPoint;
import com.github.springjwt.security.spring.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ConfigurationWebSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    @Lazy
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public DaoAuthenticationProvider customAuthenticationProvider(){
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("salt");

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setSaltSource(saltSource);

        return authenticationProvider;
    }

    @Bean
    @Override
    @Qualifier("customAuthenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.httpBasic()
                .authenticationEntryPoint(http401UnauthorizedEntryPoint)
                .and()
                    .anonymous()
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .csrf().disable();
    }
}