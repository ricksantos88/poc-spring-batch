package wendel.tech.backend.reprocess.shared.database.collections

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "collection_a")
data class EventA(
    @Id
    val id: String? = null,
    val idOperation: String,
    val eventData: LocalDateTime,
    val createdAt: LocalDateTime
)
