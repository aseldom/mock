package ru.checkdev.desc.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.desc.domain.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends CrudRepository<Topic, Integer> {
    List<Topic> findAllByOrderByPositionAsc();

    List<Topic> findByCategoryIdOrderByPositionAsc(Integer categoryId);

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Modifying
    @Query("update cd_topic t set t.total = t.total + 1 where t.id=:id")
    void incrementTotal(@Param("id") int id);

    @Query("SELECT t.name FROM cd_topic t WHERE t.id = :id")
    Optional<String> getNameById(@Param("id") int id);
}
