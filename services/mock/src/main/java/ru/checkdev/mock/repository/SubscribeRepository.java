package ru.checkdev.mock.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.checkdev.mock.domain.Subscribe;

import java.util.Optional;

@Repository
public interface SubscribeRepository extends CrudRepository<Subscribe, Long> {

    @Override
    Optional<Subscribe> findById(Long chatId);

    Subscribe save(Subscribe subscribe);

    boolean deleteByChatId(long id);

}
