package com.vsiestov.platform;

import com.vsiestov.users.services.JwtService;
import com.vsiestov.users.services.UsersService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UsersService usersService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    public SecurityConfiguration(
        UsersService usersService,
        BCryptPasswordEncoder bCryptPasswordEncoder,
        JwtService jwtService
    ) {
        this.usersService = usersService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/sign-in", "/sign-up", "/api-docs/**", "/swagger-ui/**")
            .permitAll()
//            .antMatchers("/tasks").access("@webSecurity.check(authentication, request)")
            .anyRequest().authenticated()
            .and()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService))
            .addFilter(new JwtAuthorizationFilter(authenticationManager()))
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
    }
}
