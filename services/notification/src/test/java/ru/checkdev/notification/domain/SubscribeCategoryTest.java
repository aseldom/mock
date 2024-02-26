package ru.checkdev.notification.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscribeCategoryTest {



    @Test
    public void whenDefaultConstructorNotNull() {
        SubscribeCategory subscribeCategory = new SubscribeCategory();
        assertNotNull(subscribeCategory);
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        SubscribeCategory subscribeCategory = new SubscribeCategory(0, 1, 1);
        assertNotNull(subscribeCategory);
    }

    @Test
    public void whenIDSetAndGetEquals() {
        SubscribeCategory subscribeCategory = new SubscribeCategory(0, 1, 1);
        subscribeCategory.setId(1);
        assertThat(1, is(subscribeCategory.getId()));
    }
}