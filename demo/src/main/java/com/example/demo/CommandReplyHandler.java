package com.example.demo;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class CommandReplyHandler {

    private final CommandLogRepository commandLogRepository;
    private final ObjectMapper mapper = new ObjectMapper();
    private final LiveSocketHandler socket;


    public CommandReplyHandler(CommandLogRepository commandLogRepository,LiveSocketHandler socket) {
        this.commandLogRepository = commandLogRepository;
        this.socket = socket;
    }

    public void handleReply(Message<?> message) {
        try {
            String payload = (String) message.getPayload();
           CommandLog reply = mapper.readValue(payload, CommandLog.class);
           commandLogRepository.findById(reply.getId()).ifPresent(original -> {
               original.setResult(reply.getResult());
               commandLogRepository.save(original);
               System.out.println("Command"+original.getId()+"-> result "+original.getResult());
           });
        } catch (Exception e) {
            System.err.println("Failed to parse:" + e.getMessage());
        }
    }

}
