package ru.checkdev.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.repository.PersonRepository;

import java.util.stream.Collectors;

@Component
public class UserDetailsDefinition implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsDefinition.class);

    private final PersonRepository persons;

    public UserDetailsDefinition(PersonRepository persons) {
        this.persons = persons;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws DisabledException {
        Profile profile = this.persons.findByEmail(email);
        if (profile != null) {
            if (profile.isActive()) {
                return new User(email,
                        profile.getPassword(),
                        profile.getRoles().stream()
                                .map(
                                        role -> new SimpleGrantedAuthority(role.getValue())
                                ).collect(Collectors.toList())
                ) {
                    public String getKey() {
                        return profile.getKey();
                    }
                };
            } else {
                throw new DisabledException(String.format("Пользователь с почтой %s не активирован.", email));
            }
        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}
