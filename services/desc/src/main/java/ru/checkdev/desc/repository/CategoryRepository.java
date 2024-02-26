package ru.checkdev.desc.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.desc.domain.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Iterable<Category> findAllByOrderByPositionAsc();

    @Query("from cd_category ca order by ca.total desc")
    List<Category> findAllByOrderTotalDescLimit(Pageable pageable);

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Modifying
    @Query("UPDATE cd_category c SET c.total = c.total + 1 WHERE c.id =:id")
    void updateStatistic(@Param("id") int id);
}
