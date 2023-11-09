package com.springboot.rest.services;

import com.springboot.rest.model.Monitor;
import com.springboot.rest.repositories.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MonitorService {

    @Autowired
    private MonitorRepository monitorRepository;

    public Monitor find() {
        try {
            return monitorRepository.findById("0").orElseThrow();
        } catch (NoSuchElementException e) {
            return monitorRepository.save(new Monitor());
        }
    }

    private interface Update {
        void apply(Monitor monitor);
    }

    private void update(Update update) {
        Monitor monitor = find();
        update.apply(monitor);
        monitorRepository.deleteById("0");
        monitorRepository.save(monitor);
    }

    public void incrementUserCount() {
        update(Monitor::incrementUserCount);
    }

    public void incrementOrderCount() {
        update(Monitor::incrementOrderCount);
    }

}
