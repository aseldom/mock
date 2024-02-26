package ru.checkdev.auth.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.checkdev.auth.domain.Notify;
import ru.checkdev.auth.domain.Order;
import ru.checkdev.auth.repository.OrderRepository;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikhail Epatko.
 * 25 August 2017
 * mail: mikhail.epatko@gmail.com
 */

@Service
public class OrderService {

    private final OrderRepository orders;
    private final Messenger msg;
    private final String recipient;

    @Autowired
    public OrderService(OrderRepository orders, Messenger msg,
                        final @Value("${recipient.notification}") String recipient) {
        this.orders = orders;
        this.msg = msg;
        this.recipient = recipient;
    }

    public void save(Order order) {
        order.setCreated(Calendar.getInstance());
        this.orders.save(order);
        Map<String, Object> keys = new HashMap<>();
        keys.put("order", order);
        this.msg.send(new Notify(recipient, keys, Notify.Type.ORDER.name()));
    }

    public void toArchive(Order order) {
        this.orders.updateArchive(order.getId(), true);
        order.setArchive(true);
    }

    public List<Order> findAll() {
        return Lists.newArrayList(this.orders.findAll());
    }

    public List<Order> findByType(String type) {
        if ("fresh".equals(type)) {
            return this.orders.findByArchiveOrderByCreatedDesc(false);
        } else {
            return this.orders.findAllByOrderByCreatedDesc();
        }
    }
}
