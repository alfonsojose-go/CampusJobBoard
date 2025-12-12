import com.example.CampusJobBoard.Services.JobServiceImpl;


import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.User;
import com.example.CampusJobBoard.Repositories.JobApplicationRepo;
import com.example.CampusJobBoard.Repositories.JobRepo;
import com.example.CampusJobBoard.Services.JobService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @Mock
    private JobRepo jobRepo;

    @Mock
    private JobApplicationRepo jobApplicationRepo;

    @InjectMocks
    private JobServiceImpl service; // The class containing your save method

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
    @DisplayName("Testing getting all ids")
    public void testFindAll(){
        service.findAll();

        verify(jobRepo).findAll();
    }

    @Test
    void getJobById_WhenExists_ReturnsJob() {

        Job inputJob = buildItem(20L, "Janitor");

        when(jobRepo.findById(20L)).thenReturn(Optional.of(inputJob));

        // When
        Job result = service.getJobById(20L);

        // Then
        assertEquals(20L, result.getJobId());
        verify(jobRepo).findById(20L);
    }

    @Test
    void getJobById_WhenNotExists_ThrowsException() {
        // Given
        when(jobRepo.findById(20L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> service.getJobById(20L));
        verify(jobRepo).findById(20L);
    }

    @Test
    @DisplayName("Testing approved jobs")
    public void testGetApprovedJobs(){
        service.getApprovedJobs();

        verify(jobRepo).findByStatus(Job.JobStatus.APPROVED);
    }


    @Test
    @DisplayName("Testing save data")
    public void testSaveJob() {


        Job inputJob = buildItem(null, "Janitor");
        Job savedJob = buildItem(1L, "Janitor");

        when(jobRepo.save(any(Job.class))).thenReturn(savedJob);


        // When
        Job result = service.save(inputJob);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getJobId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Janitor");
        assertThat(result.getStatus()).isEqualTo(Job.JobStatus.PENDING);

        verify(jobRepo, times(1)).save(inputJob);
    }

    @Test
    @DisplayName("Testing delete data")
    public void testDeleteById(){

        service.deleteById(20L);

        verify(jobRepo).deleteById(20L);
    }
}