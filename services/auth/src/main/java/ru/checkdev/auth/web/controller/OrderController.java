package ru.checkdev.auth.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.auth.domain.Order;
import ru.checkdev.auth.service.OrderService;

import java.util.List;

/**
 * Created by Mikhail Epatko.
 * 25 August 2017
 * mail: mikhail.epatko@gmail.com
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orders;

    @Autowired
    public OrderController(OrderService orders) {
        this.orders = orders;
    }

    @PostMapping("/save")
    public Order save(@RequestBody Order order) {
        this.orders.save(order);
        return order;
    }

    @PutMapping("/archive")
    public Order archive(@RequestBody Order order) {
        this.orders.toArchive(order);
        return order;
    }

    @GetMapping("/list")
    public List<Order> get(@RequestParam(required = false) String type) {
        return this.orders.findByType(type);
    }
}
