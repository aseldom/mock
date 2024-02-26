package ru.checkdev.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.checkdev.auth.dto.ProfileDTO;
import ru.checkdev.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

/**
 * CheckDev пробное собеседование
 * Класс получения ProfileDTO
 *
 * @author Dmitry Stepanov
 * @version 22.09.2023'T'23:41
 */

@Service
@AllArgsConstructor
@Slf4j
public class ProfileService {
    private final PersonRepository personRepository;

    /**
     * Получить ProfileDTO по ID
     *
     * @param id int
     * @return ProfileDTO
     */
    public Optional<ProfileDTO> findProfileByID(int id) {
        return Optional.ofNullable(personRepository.findProfileById(id));
    }

    /**
     * Получить список всех PersonDTO
     *
     * @return List<PersonDTO>
     */
    public List<ProfileDTO> findProfilesOrderByCreatedDesc() {
        return personRepository.findProfileOrderByCreatedDesc();
    }
}
