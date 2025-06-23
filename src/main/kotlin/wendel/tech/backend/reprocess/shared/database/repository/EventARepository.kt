package wendel.tech.backend.reprocess.shared.database.repository

import org.springframework.data.mongodb.repository.MongoRepository
import wendel.tech.backend.reprocess.shared.database.collections.EventA
import java.time.LocalDateTime

interface EventARepository: MongoRepository<EventA, String> {
    fun findByIdOperationAndEventData(idOperation: String, eventData: LocalDateTime): List<EventA>
}