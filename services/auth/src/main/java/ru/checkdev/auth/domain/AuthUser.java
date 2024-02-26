package ru.checkdev.auth.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * @author parsentev
 * @since 25.09.2016
 */
public class AuthUser extends User {
    private final List<String> rules;

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities, List<String> rules) {
        super(username, password, authorities);
        this.rules = rules;
    }

    public List<String> getRules() {
        return rules;
    }
}
