package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.JobApplication;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;
import com.example.CampusJobBoard.Repositories.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    @Autowired
    private final JobApplicationRepo jobApplicationRepo;

    @Autowired
    private final JobRepo jobRepo;

    public JobApplicationService(JobApplicationRepo jobApplicationRepo, JobRepo jobRepo) {
        this.jobApplicationRepo = jobApplicationRepo;
        this.jobRepo = jobRepo;
    }

    public List<JobApplication> getJobsWithSubmittedApplications(){
        return jobApplicationRepo.findByStatus(JobApplication.ApplicationStatus.SUBMITTED);
    }


}
