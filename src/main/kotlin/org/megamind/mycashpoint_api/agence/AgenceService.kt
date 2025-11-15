package org.megamind.mycashpoint_api.agence

import jakarta.persistence.EntityNotFoundException

import org.apache.coyote.BadRequestException
import org.megamind.mycashpoint_api.agence.dto.AgenceRequest
import org.megamind.mycashpoint_api.agence.dto.AgenceResponse
import org.megamind.mycashpoint_api.agence.dto.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AgenceService(private val repository: AgenceRepository) {


    @Transactional
    fun create(request: AgenceRequest): AgenceResponse {

        val agenceByCode = repository.findByCode(request.code)
        if (agenceByCode != null) {
            throw IllegalArgumentException("Une agence avec ce code existe")
        }

        val agence = Agence(
            code = request.code,
            name = request.name,
        )

        val saveAgence = repository.save(agence)

        return saveAgence.toResponse()

    }

    fun findByCode(code: String): AgenceResponse {

        val agence = repository.findByCode(code) ?: throw EntityNotFoundException("Agence non trouvé")
        return agence.toResponse()
    }

    fun findAll(): List<AgenceResponse> {
        return repository.findAll()
            .map { it.toResponse() }
    }

    @Transactional
    fun update(code: String, request: AgenceRequest): AgenceResponse {
        val agence = repository.findByCode(code)
            ?: throw EntityNotFoundException(
                "Agence avec le code '$code' non trouvée"
            )

        // Mise à jour (avec data class copy)
        val updated = agence.copy(name = request.name)
        val saved = repository.save(updated)

        return saved.toResponse()
    }

    @Transactional
    fun delete(code: String) {
        val agence = repository.findByCode(code)
            ?: throw EntityNotFoundException(
                "Agence avec le code '$code' non trouvée"
            )

        repository.delete(agence)
    }


}