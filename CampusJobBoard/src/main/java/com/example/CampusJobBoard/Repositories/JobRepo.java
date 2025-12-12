package com.example.CampusJobBoard.Repositories;

import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Job, Long> {

    List<Job> findByStatus(Job.JobStatus status);

    List<Job> findByEmployer(User employer);

}
