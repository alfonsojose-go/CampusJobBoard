package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.Job;
import java.util.List;
import java.util.Optional;

public interface JobService {
    List<Job> findAll();

    Optional<Job> findById(Long id);  // Use Optional here instead of returning null

    Job save(Job job);

    void deleteById(Long id);

}
