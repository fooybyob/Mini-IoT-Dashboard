package com.example.demo;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/command")
public class CommandController {

    private final MessageChannel outboundChannel;

    public CommandController(@Qualifier ("outboundChannel") MessageChannel outboundChannel) {
        this.outboundChannel = outboundChannel;
    }

    @PostMapping
    public String send(@RequestBody String command){
        outboundChannel.send(MessageBuilder.withPayload(command).build());
        return "Command sent:"+command;

    }
}
