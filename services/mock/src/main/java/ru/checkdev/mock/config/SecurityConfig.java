package ru.checkdev.mock.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/interview/**", "wisher/**")
                .hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT, "/interview/**", "wisher/**")
                .permitAll()
                .antMatchers("/interviews/", "/interviews/**", "/interviews/")
                .permitAll()
                .antMatchers("/wisher/", "wisher/**", "wishers/")
                .permitAll()
                .antMatchers("/feedback/", "feedback/**")
                .permitAll()
                .and()
                .csrf()
                .disable();
    }
}