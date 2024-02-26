package ru.checkdev.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.domain.Photo;
import ru.checkdev.auth.dto.ProfileDTO;

import java.util.List;

/**
 * @author parsentev
 * @since 25.09.2016
 */
public interface PersonRepository extends CrudRepository<Profile, Integer> {

    Profile findByEmail(String email);

    Profile findByEmailAndUsername(String email, String username);

    Profile findByKey(String key);

    List<Profile> findByKeyIn(List<String> key);

    @Modifying
    @Query("update profile p set p.username = ?1, p.password = ?2, p.experience = ?3, p.about = ?4, p.aboutShort = ?5, p.show = ?6, p.salary=?7,  p.location=?8 where p.email = ?9")
    int update(String username, String password, String experience, String about, String aboutShort, boolean show, String salary, String location, String email);

    @Modifying
    @Query("update profile p set p.username = ?1, p.password = ?2, p.experience = ?3, p.about = ?4, p.aboutShort = ?5, p.photo =?6 where p.email = ?7")
    int updateWithImg(String username, String password, String experience, String about, String aboutShort, Photo photo, String email);

    @Query("select count(p.id) from profile as p")
    Long total();

    Page findAll(Pageable pageable);

    Page findByEmailContainingOrUsernameContaining(String email, String userName, Pageable pageable);

    @Query("select new ru.checkdev.auth.domain.Profile(p.key, p.experience, p.salary, p.aboutShort, p.username, p.location, p.photo.id) from profile as p left JOIN p.photo where p.show is ?1 order by p.updated desc")
    List<Profile> findByShow(boolean show, Pageable pageable);

    @Query("select new ru.checkdev.auth.domain.Profile(p.key, p.experience, p.salary, p.aboutShort, p.username, p.location, p.photo.id) from profile as p left JOIN p.photo where p.show is ?1 order by p.updated desc")
    List<Profile> findByShow(boolean show);

    @Query("select count(p.id) from profile as p where p.show is true")
    Long showed();

    @Query("select p from profile p left join  p.photo where p.email = :email")
    Profile findPerson(@Param("email") String email);

    /**
     * Метод нативным запросом ищет пользователей по ID,
     * возвращая DTO модель ProfileDTO
     *
     * @param id int person id
     * @return ProfileDTO
     */
    @Query("SELECT new ru.checkdev.auth.dto.ProfileDTO(p.id, p.username, p.experience, p.photo.id, p.updated, p.created) FROM profile p WHERE p.id = :id")
    ProfileDTO findProfileById(@Param("id") int id);

    /**
     * Метод нативным запросом формирует список всех пользователей,
     * возвращая список DTO моделей ProfileDTO
     * сортированных по убыванию даты создания Профиля.
     *
     * @return List ProfileDTO
     */
    @Query("SELECT new ru.checkdev.auth.dto.ProfileDTO(p.id, p.username, p.experience, p.photo.id, p.updated, p.created) FROM profile p ORDER BY p.created DESC")
    List<ProfileDTO> findProfileOrderByCreatedDesc();
}
