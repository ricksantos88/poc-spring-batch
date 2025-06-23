package wendel.tech.backend.reprocess.modulecompiler.consumer

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import wendel.tech.backend.reprocess.modulecompiler.service.CompilerService
import wendel.tech.backend.reprocess.shared.database.collections.EventA

@Service
class KafkaCompilerConsumer(
    private val consumerService: CompilerService,
) {

    @KafkaListener(topics = ["topico-02"], groupId = "compiler-consumer-group")
    fun listen(event: EventA) {
        println("Received Event: $event")
        consumerService.saveEventB(event)
    }
}