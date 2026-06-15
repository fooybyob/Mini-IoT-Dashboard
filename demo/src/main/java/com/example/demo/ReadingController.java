package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/readings")
public class ReadingController {

    private final ReadingRepository repo;

    public ReadingController(ReadingRepository repo){
        this.repo = repo;
    }

    @GetMapping
    public List<Reading> latest(){
        return repo.findTop20ByOrderByTimestampDesc();
    }

    @PostMapping
    public Reading add(@RequestBody Reading r){
        return repo.save(r);
    }
}