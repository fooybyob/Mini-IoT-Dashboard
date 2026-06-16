package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertRepository repo;

    public AlertController(AlertRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Alert> latest(){
        return repo.findTop20ByOrderByTimestampDesc();
    }

    @PostMapping
    public Alert add(@RequestBody Alert a){
        return repo.save(a);
    }

}


