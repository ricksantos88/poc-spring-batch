package wendel.tech.backend.reprocess.shared.database.repository

import org.springframework.data.mongodb.repository.MongoRepository
import wendel.tech.backend.reprocess.shared.database.collections.EventB
import java.time.LocalDateTime

interface EventBRepository: MongoRepository<EventB, String> {
    fun findByIdOperationAndEventData(idOperation: String, eventData: LocalDateTime): List<EventB>
}