package wendel.tech.backend.reprocess.shared.database.collections

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "collection_b")
data class EventB(
    @Id
    val id: String? = null,
    val idOperation: String,
    val eventData: LocalDateTime,
    val enrichedData: String,
    val processedAt: LocalDateTime
) {
    constructor(eventA: EventA): this(
        id = eventA.id,
        idOperation = eventA.idOperation,
        eventData = eventA.eventData,
        enrichedData = "Enriched data based on ${eventA.eventData}",
        processedAt = LocalDateTime.now()
    )
}