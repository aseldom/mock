package ru.checkdev.mock.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.checkdev.mock.domain.Subscribe;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Repository
public interface SubscribeRepository extends CrudRepository<Subscribe, Long> {

    @NotNull
    Subscribe save(Subscribe subscribe);

    @Transactional
    int deleteByChatId(Long chatId);

}
