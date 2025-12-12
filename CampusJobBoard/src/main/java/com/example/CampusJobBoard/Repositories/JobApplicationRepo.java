package com.example.CampusJobBoard.Repositories;

import com.example.CampusJobBoard.Models.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplication, Long> {

    boolean existsByJob_JobIdAndStudent_UserId(Long jobId, Long studentId);

    List<JobApplication> findByStudent_UserId(Long studentId);
}


