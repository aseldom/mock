package ru.checkdev.mock.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.mock.domain.Filter;

import java.util.Optional;

public interface FilterRepository extends CrudRepository<Filter, Integer> {

    Optional<Filter> getByUserId(int userId);

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    int deleteByUserId(int userId);
}
