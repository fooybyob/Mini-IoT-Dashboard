package com.example.demo;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class MqttHandler {

    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public void handle(Message<?> message) {
        String payload = (String) message.getPayload();
        System.out.println("MQTT received:"+payload);

    }


}
