package wendel.tech.backend.reprocess.modulereader.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import wendel.tech.backend.reprocess.shared.database.collections.EventA

@Service
class KafkaReaderProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun sendMessage(topic: String, event: EventA) {
        kafkaTemplate.send(topic, event.idOperation, event.toString())
    }
}
