package com.example.demo;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepo;

    public DeviceService(DeviceRepository deviceRepo) {
        this.deviceRepo = deviceRepo;
    }


    public void touch(Reading reading) {
        Optional<Device> device = deviceRepo.findBySensorId(reading.getSensorId());
        if(device.isEmpty()){
            Device device_new = new Device();
            device_new.setSensorId(reading.getSensorId());
            device_new.setLastSeen(reading.getTimestamp());
            device_new.setLastSeenLong(reading.getTimestampLong());
            device_new.setBoundWorkspace("default");
            device_new.setStatus(Device.Status.ONLINE);
            deviceRepo.save(device_new);
        }
        else{
            Device d = device.get();
            d.setLastSeen(reading.getTimestamp());
            d.setLastSeenLong(reading.getTimestampLong());
            d.setStatus(Device.Status.ONLINE);
            deviceRepo.save(d);

        }
    }

    @Scheduled(fixedRate = 5000)
    public void offlineDetectionDevice() {
        for(Device d : deviceRepo.findAll()){
            if(System.currentTimeMillis()-d.getLastSeenLong() > 10000 && d.getStatus() == Device.Status.ONLINE){
                d.setStatus(Device.Status.OFFLINE);
                deviceRepo.save(d);
            }

        }
    }
}
