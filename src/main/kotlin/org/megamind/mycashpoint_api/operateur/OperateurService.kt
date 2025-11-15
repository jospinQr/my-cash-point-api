package org.megamind.mycashpoint_api.operateur

import jakarta.persistence.EntityNotFoundException
import org.megamind.mycashpoint_api.operateur.dto.OperateurRequest
import org.megamind.mycashpoint_api.operateur.dto.OperateurResponse
import org.megamind.mycashpoint_api.operateur.dto.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OperateurService(private val repository: OperateurRepository) {

    @Transactional
    fun create(request: OperateurRequest): OperateurResponse {
        val operateurByName = repository.findByName(request.name)
        if (operateurByName != null) {
            throw IllegalArgumentException("Un opérateur avec ce nom existe déjà")
        }

        val operateur = Operateur(
            name = request.name
        )

        val savedOperateur = repository.save(operateur)
        return savedOperateur.toResponse()
    }

    fun findById(id: Int): OperateurResponse {
        val operateur = repository.findById(id)
            .orElseThrow { EntityNotFoundException("Opérateur avec l'id '$id' non trouvé") }
        return operateur.toResponse()
    }

    fun findAll(): List<OperateurResponse> {
        return repository.findAll()
            .map { it.toResponse() }
    }

    @Transactional
    fun update(id: Int, request: OperateurRequest): OperateurResponse {
        val operateur = repository.findById(id)
            .orElseThrow { EntityNotFoundException("Opérateur avec l'id '$id' non trouvé") }

        val updated = operateur.copy(name = request.name)
        val saved = repository.save(updated)

        return saved.toResponse()
    }

    @Transactional
    fun delete(id: Int) {
        val operateur = repository.findById(id)
            .orElseThrow { EntityNotFoundException("Opérateur avec l'id '$id' non trouvé") }
        repository.delete(operateur)
    }
}

