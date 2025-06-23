package wendel.tech.backend.reprocess.modulereprocess.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.MongoItemReader
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.transaction.PlatformTransactionManager
import wendel.tech.backend.reprocess.shared.database.collections.EventA
import wendel.tech.backend.reprocess.shared.database.collections.EventB
import java.time.LocalDate
import java.time.LocalTime

@Configuration
@EnableBatchProcessing
class ReprocessBatchConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val mongoTemplate: MongoTemplate,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    // Job definition
    @Bean
    fun reprocessJob(): Job {
        return JobBuilder("reprocessJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(cleanCollectionBStep())
            .next(reprocessEventsStep())
            .build()
    }

    // Step 1: Limpar a collection B
    @Bean
    fun cleanCollectionBStep(): Step {
        return StepBuilder("cleanCollectionBStep", jobRepository)
            .tasklet({ contribution, chunkContext ->
                val parameters = chunkContext.stepContext.jobParameters
                val idOperation = parameters["idOperation"] as String

                val deleteResult = mongoTemplate.remove(
                    Query.query(Criteria.where("idOperation").`is`(idOperation)),
                    EventB::class.java
                )

                println("Deleted ${deleteResult.deletedCount} records from collection B")
                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }

    // Step 2: Reprocessar eventos da collection A
    @Bean
    fun reprocessEventsStep(): Step {
        return StepBuilder("reprocessEventsStep", jobRepository)
            .chunk<EventA, EventA>(10, transactionManager)
            .reader(eventReader(null, null, null))
            .processor(eventProcessor())
            .writer(eventWriter())
            .build()
    }

    // Reader: Lê eventos da collection A
    @Bean
    @StepScope
    fun eventReader(
        @Value("#{jobParameters['idOperation']}") idOperation: String?,
        @Value("#{jobParameters['startDate']}") startDate: LocalDate?,
        @Value("#{jobParameters['endDate']}") endDate: LocalDate?
    ): ItemReader<EventA> {
        val query = Query().apply {
            if (!idOperation.isNullOrBlank()) {
                addCriteria(Criteria.where("idOperation").`is`(idOperation))
            }
            if (startDate != null && endDate != null) {
                addCriteria(
                    Criteria.where("createdAt").gte(startDate.atStartOfDay())
                    .lte(endDate.atTime(LocalTime.MAX)))
            }
        }.with(Sort.by(Sort.Direction.ASC, "createdAt"))

        return MongoItemReader<EventA>().apply {
            setTemplate(mongoTemplate)
            setTargetType(EventA::class.java)
            setQuery(query)
            setSort(mapOf("createdAt" to Sort.Direction.ASC))
        }
    }

    // Processor: Pode fazer transformações se necessário
    @Bean
    fun eventProcessor(): ItemProcessor<EventA, EventA> {
        return ItemProcessor { event -> event }
    }

    // Writer: Envia eventos para o tópico Kafka
    @Bean
    fun eventWriter(): ItemWriter<EventA> {
        val topicToResend = "topico-02"
        return ItemWriter { events ->
            events.forEach { event ->
                kafkaTemplate.send(topicToResend, event.toString())
                println("Sent event ${event.id} to Kafka topic: $topicToResend")
            }
        }
    }
}