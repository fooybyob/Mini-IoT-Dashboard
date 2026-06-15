package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReadingRepository extends JpaRepository<Reading,Long> {
    List<Reading> findTop20ByOrderByTimestampDesc(); //Spring writes the SQL for you
}
