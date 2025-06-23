package wendel.tech.backend.reprocess.modulecompiler.service

import org.springframework.stereotype.Service
import wendel.tech.backend.reprocess.shared.database.collections.EventA
import wendel.tech.backend.reprocess.shared.database.collections.EventB
import wendel.tech.backend.reprocess.shared.database.repository.EventBRepository

@Service
class CompilerService(
    private val eventBRepository: EventBRepository
) {
    
    fun saveEventB(eventA: EventA) {
        val eventB = EventB(eventA,)
        eventBRepository.save(eventB)
    }
}