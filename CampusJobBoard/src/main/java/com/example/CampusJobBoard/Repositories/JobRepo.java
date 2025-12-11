package com.example.CampusJobBoard.Repositories;

import com.example.CampusJobBoard.Models.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepo extends JpaRepository<Job, Long> {
    List<Job> findByStatus(Job.JobStatus status);
}

