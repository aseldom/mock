package ru.checkdev.auth.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.checkdev.auth.domain.Order;
import ru.checkdev.auth.repository.OrderRepository;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;

    private static final Order ORDER_ONE = new Order(
            1,
            1,
            "email@email.ru",
            "User1",
            "message",
            Calendar.getInstance(),
            true);
    private static final Order ORDER_TWO = new Order(
            2,
            2,
            "email@email.ru",
            "User2",
            "message",
            Calendar.getInstance(),
            true);
    private static final Order ORDER_THREE = new Order(
            3,
            3,
            "email@email.ru",
            "User3",
            "message",
            Calendar.getInstance(),
            false);

    @Test
    void whenFindByTypeArchive() {
        when(orderRepository.findByArchiveOrderByCreatedDesc(false))
                .thenReturn(List.of(ORDER_THREE));
        assertEquals(1, orderService.findByType("fresh").size());
    }

    @Test
    void whenFindByTypeAll() {
        when(orderRepository.findAllByOrderByCreatedDesc())
                .thenReturn(List.of(ORDER_ONE, ORDER_TWO, ORDER_THREE));
        assertEquals(3, orderService.findByType("some text").size());
    }

}