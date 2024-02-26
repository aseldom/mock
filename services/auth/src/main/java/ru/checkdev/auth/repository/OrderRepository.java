package ru.checkdev.auth.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.auth.domain.Order;

import java.util.List;

/**
 * Created by Mikhail Epatko.
 * 25 August 2017
 * mail: mikhail.epatko@gmail.com
 */
public interface OrderRepository extends CrudRepository<Order, Integer> {

    List<Order> findAllByOrderByCreatedDesc();

    List<Order> findByArchiveOrderByCreatedDesc(boolean archive);

    @Modifying
    @Transactional
    @Query("update orders o set o.archive = ?2 where o.id = ?1")
    void updateArchive(int id, boolean archive);
}
