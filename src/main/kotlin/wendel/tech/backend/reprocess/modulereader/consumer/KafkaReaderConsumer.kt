package wendel.tech.backend.reprocess.modulereader.consumer

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import wendel.tech.backend.reprocess.modulereader.service.ReaderService
import wendel.tech.backend.reprocess.shared.database.collections.EventA

@Service
class KafkaReaderConsumer(
    private val readerService: ReaderService,
) {

    @KafkaListener(topics = ["topico-01"], groupId = "reader-consumer-group")
    fun listen(event: EventA) {
        println("Received Event: $event")
        readerService.saveEventA(event)
    }
}
