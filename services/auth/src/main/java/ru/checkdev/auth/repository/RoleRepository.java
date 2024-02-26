package ru.checkdev.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.auth.domain.Role;

/**
 * @author parsentev
 * @since 25.09.2016
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
