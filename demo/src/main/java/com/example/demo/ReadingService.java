package com.example.demo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReadingService {
    private final ReadingRepository repo;
    private final AlertRepository alertRepo;
    private final DeviceService deviceService;
    private static final double THRESHOLD = 23.0;

    public ReadingService(ReadingRepository repo, AlertRepository alertRepo,  DeviceService deviceService) {
        this.repo=repo;
        this.alertRepo=alertRepo;
        this.deviceService=deviceService;
    }

    public Reading saveReading(Reading reading) {
        if(reading.getTemperature()>THRESHOLD){
            Alert alert = new Alert();
            alert.setSensorId(reading.getSensorId());
            alert.setTemperature(reading.getTemperature());
            alert.setTimestamp(reading.getTimestamp());
            alertRepo.save(alert);

        }
        deviceService.touch(reading);
        return repo.save(reading);
    }




}
