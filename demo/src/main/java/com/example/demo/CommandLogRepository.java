package com.example.demo;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommandLogRepository extends JpaRepository<CommandLog,Long> {
    Optional<CommandLog> findByTid(String tid);
}
