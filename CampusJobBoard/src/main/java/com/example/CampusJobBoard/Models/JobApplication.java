package com.example.CampusJobBoard.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "JOB_APPLICATION",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_student_job",
                columnNames = {"job_id", "student_id"}
        ))
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_Id")
    private Long applicationId;


    //Correct foreign key relationship to Job
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    @NotNull
    private Job job;  // Job entity


    // Correct foreign key relationship to Student (User)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_Id", nullable = false)
    @NotNull
    private User student;  // User entity


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @CreationTimestamp
    @Column(name = "applied_at", nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    // Enum definition
    public enum ApplicationStatus {
        SUBMITTED, ACCEPTED, REJECTED
    }

    // Constructors
    public JobApplication() {
    }

    public JobApplication(Job job, User student, ApplicationStatus status) {
        this.job = job;
        this.student = student;
        this.status = status;
    }

    //Getters and Setters

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
}
