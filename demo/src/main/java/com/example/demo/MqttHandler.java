package com.example.demo;

import tools.jackson.databind.ObjectMapper;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class MqttHandler {

    private final ReadingRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    public MqttHandler(ReadingRepository repo) {
        this.repo = repo;
    }


    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public void handle(Message<?> message) {
        try {
            String payload = (String) message.getPayload();
            Reading reading = mapper.readValue(payload, Reading.class);
            repo.save(reading);
            System.out.println("Saved reading:"+reading.getTemperature()+"°C");
        }
        catch (Exception e) {
            System.err.println("Failed to parse:"+e.getMessage());
        }
    }


}
