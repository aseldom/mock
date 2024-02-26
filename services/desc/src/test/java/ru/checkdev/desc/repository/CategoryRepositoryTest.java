package ru.checkdev.desc.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.desc.domain.Category;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@RunWith(SpringRunner.class)
class CategoryRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void clearTable() {
        entityManager.createQuery("delete from cd_topic").executeUpdate();
        entityManager.createQuery("delete from cd_category").executeUpdate();
        entityManager.clear();
    }

    @Test
    void initRepositoryWhenNotNull() {
        assertThat(categoryRepository).isNotNull();
    }

    @Test
    void whenCreatedNewCategory() {
        var category = new Category();
        category.setName("Java SE");
        var saved = categoryRepository.save(category);
        assertThat(
                categoryRepository.findById(saved.getId())
        ).isEqualTo(Optional.of(saved));
    }

    @Test
    void whenFindAllByOrderByPositionAscThenReturnAscList() {
        var category1 = new Category();
        category1.setName("category1");
        category1.setPosition(44);
        var category2 = new Category();
        category2.setName("category2");
        category2.setPosition(4);
        entityManager.persist(category1);
        entityManager.persist(category2);
        entityManager.clear();
        var expectedList = List.of(category2, category1);
        var actualList = categoryRepository.findAllByOrderByPositionAsc();
        assertThat(actualList).usingRecursiveComparison().isEqualTo(expectedList);
    }

    @Test
    void whenUpdateStatistic2ThenReturnStatistic2() {
        int expectTotal = 2;
        var category1 = new Category();
        category1.setName("category1");
        category1.setPosition(44);
        entityManager.persist(category1);
        entityManager.clear();
        categoryRepository.updateStatistic(category1.getId());
        categoryRepository.updateStatistic(category1.getId());
        var categoryInDb = categoryRepository.findById(category1.getId());
        assertThat(categoryInDb).isNotEmpty();
        assertThat(categoryInDb.get().getTotal()).isEqualTo(expectTotal);
    }

    @Test
    void whenUpdateStatistic2ThenReturnStatistic5() {
        int expectTotal = 5;
        var category1 = new Category();
        category1.setName("category1");
        category1.setPosition(44);
        entityManager.persist(category1);
        entityManager.clear();
        categoryRepository.updateStatistic(category1.getId());
        categoryRepository.updateStatistic(category1.getId());
        categoryRepository.updateStatistic(category1.getId());
        categoryRepository.updateStatistic(category1.getId());
        categoryRepository.updateStatistic(category1.getId());
        var categoryInDb = categoryRepository.findById(category1.getId());
        assertThat(categoryInDb).isNotEmpty();
        assertThat(categoryInDb.get().getTotal()).isEqualTo(expectTotal);
    }

    @Test
    void whenAdd2CategoryFindAllByOrderTotalDescLimit1ThenReturnSize1() {
        var limit = 1;
        var category1 = new Category();
        category1.setName("category1");
        category1.setTotal(20);
        var category2 = new Category();
        category2.setName("category2");
        category2.setTotal(44);
        entityManager.persist(category1);
        entityManager.persist(category2);
        entityManager.clear();
        var actual = categoryRepository.findAllByOrderTotalDescLimit(PageRequest.of(0, limit));
        var expect = List.of(category2);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenAdd2CategoryFindAllByOrderTotalDescLimit2ThenReturnSize2() {
        var limit = 2;
        var category1 = new Category();
        category1.setName("category1");
        category1.setTotal(20);
        var category2 = new Category();
        category2.setName("category2");
        category2.setTotal(44);
        entityManager.persist(category1);
        entityManager.persist(category2);
        entityManager.clear();
        var actual = categoryRepository.findAllByOrderTotalDescLimit(PageRequest.of(0, limit));
        var expect = List.of(category2, category1);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenAdd5CategoryFindAllByOrderTotalDescLimit4ThenReturnSize42() {
        var limit = 4;
        var category1 = new Category();
        category1.setName("category1");
        category1.setTotal(20);
        var category2 = new Category();
        category2.setName("category2");
        category2.setTotal(44);
        var category3 = new Category();
        category3.setName("category3");
        category3.setTotal(15);
        var category4 = new Category();
        category4.setName("category4");
        category4.setTotal(60);
        var category5 = new Category();
        category5.setName("category5");
        category5.setTotal(3);
        entityManager.persist(category1);
        entityManager.persist(category2);
        entityManager.persist(category3);
        entityManager.persist(category4);
        entityManager.persist(category5);
        entityManager.clear();
        var actual = categoryRepository.findAllByOrderTotalDescLimit(PageRequest.of(0, limit));
        var expect = List.of(category4, category2, category1, category3);
        assertThat(actual).isEqualTo(expect);
    }
}