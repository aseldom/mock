package ru.checkdev.notification.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscribeTopicTest {

    @Test
    public void whenDefaultConstructorNotNull() {
        SubscribeTopic subscribeTopic = new SubscribeTopic();
        assertNotNull(subscribeTopic);
    }

    @Test
    public void whenFieldsConstructorNotNull() {
        SubscribeTopic subscribeTopic = new SubscribeTopic(0, 1, 1);
        assertNotNull(subscribeTopic);
    }

    @Test
    public void whenIDSetAndGetEquals() {
        SubscribeTopic subscribeTopic = new SubscribeTopic(0, 1, 1);
        subscribeTopic.setId(1);
        assertThat(1, is(subscribeTopic.getId()));
    }
}