package com.example.CampusJobBoard.Services;

import com.example.CampusJobBoard.Models.JobApplication;
import java.util.List;

public interface StudentService {
    List<JobApplication> getMyApplications(String email);
}
