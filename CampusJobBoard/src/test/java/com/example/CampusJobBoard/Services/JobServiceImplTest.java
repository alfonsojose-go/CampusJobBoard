import com.example.CampusJobBoard.Services.JobServiceImpl;


import com.example.CampusJobBoard.Models.Job;
import com.example.CampusJobBoard.Models.User;
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

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @Mock
    private JobRepo jobRepo;

    @InjectMocks
    private JobServiceImpl service; // The class containing your save method

    // ---------------------------------------------
    // Helper method to generate a valid LostItem
    // ---------------------------------------------
    private Job buildItem(Long id, String title){
        // Given
        Job job = new Job();
        job.setJobId(id);
        job.setTitle(title);
        job.setDescription("cleaning and sanitizing floors");
        job.setCategory("housekeeping");
        job.setLocation("Red Deer");
        job.setSalary(BigDecimal.valueOf(15.0));
        job.setStatus(Job.JobStatus.valueOf("APPROVED"));

        User employer = new User();
        employer.setUserId(id != null ? id : 1L); // dummy employer for test
        job.setEmployer(employer);

        return job;
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
        assertThat(result.getStatus()).isEqualTo(Job.JobStatus.APPROVED);


        verify(jobRepo, times(1)).save(inputJob);
    }
}