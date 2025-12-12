package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;
import com.example.CampusJobBoard.Repositories.JobRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private final JobRepo jobRepo;

    private final JobApplicationRepo jobApplicationRepo;

    public JobServiceImpl(JobRepo jobRepo, JobApplicationRepo jobApplicationRepo) {
        this.jobRepo = jobRepo;
        this.jobApplicationRepo = jobApplicationRepo;
    }

    @Override
    public List<Job> findAll() {
        return jobRepo.findAll();
    }

    @Override
    public Optional<Job> findById(Long id) {
        return jobRepo.findById(id);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @Override
    public List<Job> getApprovedJobs() {
        // Call the String-based finder
        return jobRepo.findByStatus(Job.JobStatus.APPROVED);
    }

    @Override
    public Job save(Job job) {
        return jobRepo.save(job);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        jobApplicationRepo.deleteById(id);
        jobRepo.deleteById(id);
    }
}
