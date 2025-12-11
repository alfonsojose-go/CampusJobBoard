package com.example.CampusJobBoard.Repositories;

import com.example.CampusJobBoard.Models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JobRepo extends JpaRepository<Job, Long> {


}
