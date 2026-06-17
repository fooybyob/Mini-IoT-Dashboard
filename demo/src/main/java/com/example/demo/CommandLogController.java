package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/commands")
public class CommandLogController {

    private final CommandLogRepository commandLogRepository;

    public CommandLogController(CommandLogRepository commandLogRepository) {
        this.commandLogRepository = commandLogRepository;
    }

    @GetMapping
    public List<CommandLog> getCommandLogs() {
        return commandLogRepository.findAll();
    }
}
