package ru.checkdev.mock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.mock.domain.Interview;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Integer> {

    Optional<Interview> findById(int id);

    List<Interview> findByMode(int mode);

    Page<Interview> findByTopicId(int topicId, Pageable pageable);

    /**
     * Метод обновляет статус собеседования.
     *
     * @param id     ID Interview
     * @param status Status
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE interview i SET i.status=:status WHERE i.id=:id")
    void updateStatus(@Param("id") int id, @Param("status") int status);

    Page<Interview> findByTopicIdIn(Collection<Integer> topicIds, Pageable pageable);
}
