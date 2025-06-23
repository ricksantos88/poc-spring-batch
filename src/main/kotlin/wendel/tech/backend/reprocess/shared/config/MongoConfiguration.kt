package wendel.tech.backend.reprocess.shared.config

import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongoConfiguration {
    @Value("\${spring.data.mongodb.uri}")
    private lateinit var mongoUri: String

    @Value("\${spring.data.mongodb.database}")
    private lateinit var databaseName: String

    @Bean
    fun mongoTemplate(): MongoTemplate {
        val client = MongoClients.create(mongoUri)
        return MongoTemplate(client, databaseName)
    }
}