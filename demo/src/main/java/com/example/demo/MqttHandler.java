package com.example.demo;

import tools.jackson.databind.ObjectMapper;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;


@Service
public class MqttHandler {
    private final ReadingService readingService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final LiveSocketHandler socket;
    public MqttHandler(ReadingService readingService,LiveSocketHandler socket) {
        this.readingService=readingService;
        this.socket = socket;
    }

    public void handle(Message<?> message) {
        try {
            String payload = (String) message.getPayload();
            Reading reading = mapper.readValue(payload, Reading.class);
            readingService.saveReading(reading);
            socket.broadcast(payload);
            System.out.println("Saved reading:" + reading.getTemperature() + "°C");
        } catch (Exception e) {
            System.err.println("Failed to parse:" + e.getMessage());
        }
    }
}



