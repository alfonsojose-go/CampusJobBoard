package com.example.CampusJobBoard.Controllers;

import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;
import com.example.CampusJobBoard.Repositories.JobRepo;
import com.example.CampusJobBoard.Services.JobApplicationService;
import com.example.CampusJobBoard.Services.JobService;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(JobController.class)
@AutoConfigureMockMvc(addFilters = false) // This disables security filters
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService service;

    @MockitoBean
    private JobRepo repo;

    @MockitoBean
    private JobApplicationService jobApplicationService;

    @MockitoBean
    private JobApplicationRepo jobApplicationRepo;

    // ---------------------------------------------
    // Helper method to generate a valid job
    // ---------------------------------------------
    private Job buildItem(Long id, String title){
        // Given

        //use this code for now
        User tempEmployer = new User();
        tempEmployer.setUserId(1L);


        Job job = new Job();
        job.setJobId(id);
        job.setTitle(title);
        job.setDescription("cleaning and sanitizing floors");
        job.setCategory("housekeeping");
        job.setLocation("Red Deer");
        job.setSalary(BigDecimal.valueOf(15.0));
        job.setStatus(Job.JobStatus.PENDING);
        job.setEmployer(tempEmployer);
        return job;
    }

    @Test
    @DisplayName("Testing job postings")
    public void testListJobs() throws Exception{
        User tempEmployer = new User();
        tempEmployer.setUserId(1L);

        List<Job> jobList = List.of(
                new Job(tempEmployer,
                        "Janitor",
                        "cleans",
                        "Red Deer",
                        new BigDecimal("15.50"),
                        "Hotel",
                        LocalDate.of(2025, 12, 25)),
                new Job(tempEmployer,
                        "Front Desk",
                        "cleans",
                        "Red Deer",
                        new BigDecimal("15.50"),
                        "Hotel",
                        LocalDate.of(2025, 12, 25))

        );

        when(service.findAll()).thenReturn(jobList);

        mockMvc.perform(get("/job"))
                .andExpect(status().isOk())
                .andExpect(view().name("job-list"))
                .andExpect(model().attributeExists("jobs"));
    }

    @Test
    @DisplayName("Testing Creation of Job Posting")
    public void  testShowForm() throws Exception {
        mockMvc.perform(get("/job/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("job-form"))
                .andExpect(model().attributeExists("jobs"));
    }

    @Test
    @DisplayName("Testing Saving Job")
    public void testSaveJob() throws Exception{
        Job savedJob = buildItem(1L, "Janitor");

        when(service.save(any(Job.class))).thenReturn(savedJob);

        mockMvc.perform(post("/job/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Janitor")
                .param("description", "cleaning and sanitizing floors")
                .param("category", "housekeeping")
                .param("location", "Red Deer")
                .param("salary", "15.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/job"));
    }

    @Test
    @DisplayName("Testing DELETE job")
    public void testDeleteJob() throws Exception {
        mockMvc.perform(get("/job/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/job"));
    }

    @Test
    @DisplayName("Testing show edit form")
    public void testShowEditForm() throws Exception {
        // Given
        Job job = buildItem(1L, "Janitor");
        when(service.findById(1L)).thenReturn(java.util.Optional.of(job));

        // When & Then
        mockMvc.perform(get("/job/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("job-form"))
                .andExpect(model().attributeExists("jobs"))
                .andExpect(model().attribute("jobs", job));
    }
}