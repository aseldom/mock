package ru.checkdev.notification.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.notification.domain.SubscribeCategory;

import java.util.List;

public interface SubscribeCategoryRepository extends CrudRepository<SubscribeCategory, Integer> {
    @Override
    List<SubscribeCategory> findAll();

    List<SubscribeCategory> findByUserId(int id);

    SubscribeCategory findByUserIdAndCategoryId(int userId, int categoryId);
}