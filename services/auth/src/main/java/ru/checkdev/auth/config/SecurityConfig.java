package ru.checkdev.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableWebSecurity
@EnableResourceServer
@EnableAuthorizationServer
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/ping",
                "/registration",
                "/forgot",
                "/auth/activated/**",
                "/person/by",
                "/person/random",
                "/person/resume/**",
                "/order/save",
                "/person/by/email",
                "/img",
                "/person/profile",
                "/template/queue",
                "/template/ping",
                "/profiles/**",
                "/swagger-ui/**",
                "/v3/**"
        );
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());

    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
