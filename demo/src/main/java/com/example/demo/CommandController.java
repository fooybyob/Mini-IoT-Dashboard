package com.example.demo;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@RestController
@RequestMapping("/api/command")
public class CommandController {

    private final MessageChannel outboundChannel;
    private final CommandLogRepository commandLogRepository;

    public CommandController(@Qualifier ("outboundChannel") MessageChannel outboundChannel, CommandLogRepository commandLogRepository ) {
        this.outboundChannel = outboundChannel;
        this.commandLogRepository = commandLogRepository;

    }

    @PostMapping
    public String send(@RequestParam String sensorId, @RequestParam String method, @RequestParam String value ) {

        String tid = UUID.randomUUID().toString();

        CommandLog log = new CommandLog();
        log.setTid(tid);
        log.setMethod(method);
        log.setSensorId(sensorId);
        log.setResult("PENDING");
        log.setTimeStampLong(System.currentTimeMillis());
        log.setTimeStamp(java.time.Instant.now().toString());

        commandLogRepository.save(log);

        String envelope = new ObjectMapper().writeValueAsString(java.util.Map.of("tid",tid,"method",method,"data",value));
        outboundChannel.send(MessageBuilder.withPayload(envelope).build());
        return "Command sent, tid:"+tid;

    }

}
