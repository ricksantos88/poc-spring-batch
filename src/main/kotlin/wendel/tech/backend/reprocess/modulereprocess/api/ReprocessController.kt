package wendel.tech.backend.reprocess.modulereprocess.api

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/reprocess")
class ReprocessController(
    private val jobLauncher: JobLauncher,
    @Qualifier("reprocessJob") private val reprocessJob: Job
) {

    @PostMapping
    fun startReprocess(
        @RequestBody reprocessRequest: ReprocessRequest
    ): ResponseEntity<String> {
        val jobParameters = JobParametersBuilder()
            .addString("idOperation", reprocessRequest.idOperation)
            .addLocalDate("startDate", reprocessRequest.startDate.toLocalDate())
            .addLocalDate("endDate", reprocessRequest.endDate.toLocalDate())
            .addLong("time", System.currentTimeMillis())
            .toJobParameters()

        try {
            val jobExecution = jobLauncher.run(reprocessJob, jobParameters)
            return ResponseEntity.ok("Job started with ID: ${jobExecution.jobId}")
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error starting job: ${e.message}")
        }
    }
}

data class ReprocessRequest(
    val idOperation: String,
    @field:JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startDate: LocalDateTime,
    @field:JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endDate: LocalDateTime
)
