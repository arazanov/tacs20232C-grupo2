package com.springboot.rest.controllers;

import com.springboot.rest.services.OrderService;
import com.springboot.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping
    public MonitorResponse getMonitor() {
        return new MonitorResponse(
                userService.userCount(),
                orderService.orderCount()
        );
    }

    public record MonitorResponse(long userCount, long orderCount) {
    }

}
