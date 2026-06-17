package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReadingService {
    private final ReadingRepository repo;
    private final AlertRepository alertRepo;
    private final DeviceRepository deviceRepo;
    private static final double THRESHOLD = 23.0;

    public ReadingService(ReadingRepository repo, AlertRepository alertRepo,DeviceRepository deviceRepo) {
        this.repo=repo;
        this.alertRepo=alertRepo;
        this.deviceRepo=deviceRepo;
    }

    public Reading saveReading(Reading reading) {
        if(reading.getTemperature()>THRESHOLD){
            Alert alert = new Alert();
            alert.setSensorId(reading.getSensorId());
            alert.setTemperature(reading.getTemperature());
            alert.setTimestamp(reading.getTimestamp());
            alertRepo.save(alert);

        }
        touch(reading);
        return repo.save(reading);
    }


    private void touch(Reading reading) {
        Optional<Device> device = deviceRepo.findBySensorId(reading.getSensorId());
        if(device.isEmpty()){
            Device device_new = new Device();
            device_new.setSensorId(reading.getSensorId());
            device_new.setLastSeen(reading.getTimestamp());
            device_new.setBoundWorkspace("default");
            device_new.setStatus(Device.Status.ONLINE);
            deviceRepo.save(device_new);
        }
        else{
            Device d = device.get();
            d.setLastSeen(reading.getTimestamp());
            d.setStatus(Device.Status.ONLINE);
            deviceRepo.save(d);

        }
    }



}
