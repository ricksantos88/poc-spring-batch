package wendel.tech.backend.reprocess.shared.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
class BatchMongoConfig {

    @Bean
    fun transactionManager(): PlatformTransactionManager {
        return ResourcelessTransactionManager() // Não requer transações JDBC
    }

    @Bean
    fun jobRepository(): JobRepositoryFactoryBean {
        val factory = JobRepositoryFactoryBean()
        factory.transactionManager = transactionManager()
        return factory
    }
}