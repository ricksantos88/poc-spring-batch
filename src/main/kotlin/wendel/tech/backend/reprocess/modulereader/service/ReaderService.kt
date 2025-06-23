package wendel.tech.backend.reprocess.modulereader.service

import org.springframework.stereotype.Service
import wendel.tech.backend.reprocess.modulereader.producer.KafkaReaderProducer
import wendel.tech.backend.reprocess.shared.database.collections.EventA
import wendel.tech.backend.reprocess.shared.database.repository.EventARepository

@Service
class ReaderService(
    private val eventARepository: EventARepository,
    private val producer: KafkaReaderProducer
) {

    fun saveEventA(eventData: EventA) {

        eventARepository.save(eventData)
        producer.sendMessage("topico-02", eventData)
    }
}