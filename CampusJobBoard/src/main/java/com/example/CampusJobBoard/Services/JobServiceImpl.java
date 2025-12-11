package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Repositories.JobRepo;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepo jobRepo;

    public JobServiceImpl(JobRepo jobRepo) {
        this.jobRepo = jobRepo;
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
        return jobRepo.findByStatus(Job.JobStatus.APPROVED);
    }


    @Override
    public Job save(Job job) {
        return jobRepo.save(job);
    }

    @Override
    public void deleteById(Long id) {
        jobRepo.deleteById(id);
    }
}
