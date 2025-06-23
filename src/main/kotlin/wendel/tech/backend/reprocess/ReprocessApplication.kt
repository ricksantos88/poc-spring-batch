package wendel.tech.backend.reprocess

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class ReprocessApplication

fun main(args: Array<String>) {
	runApplication<ReprocessApplication>(*args)
}
