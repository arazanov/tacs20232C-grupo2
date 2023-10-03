package com.springboot.rest.controllers;

import com.springboot.rest.model.Monitor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorController {

    @GetMapping("/monitor")
    public ResponseEntity<Monitor> getOrderById() {
        return ResponseEntity.ok(Monitor.getInstance());
    }

}
