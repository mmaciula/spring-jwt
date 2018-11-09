package com.github.springjwt.config;

import com.github.springjwt.security.jwt.filter.TokenAuthenticationFilter;
import com.github.springjwt.security.jwt.service.DaoUserDetailService;
import com.github.springjwt.security.spring.Http401UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfigurationWebSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint;
    @Autowired
    private DaoUserDetailService daoUserDetailService;

    // TODO Authorization token

    @Autowired
    @Lazy
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public DaoAuthenticationProvider customAuthenticationProvider(){

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

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