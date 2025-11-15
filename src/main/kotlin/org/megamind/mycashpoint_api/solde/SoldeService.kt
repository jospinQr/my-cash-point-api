package org.megamind.mycashpoint_api.solde

import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.megamind.mycashpoint_api.agence.AgenceRepository

import org.megamind.mycashpoint_api.operateur.OperateurRepository
import org.megamind.mycashpoint_api.solde.dto.SoldeRequest
import org.megamind.mycashpoint_api.solde.dto.SoldeResponse
import org.megamind.mycashpoint_api.solde.dto.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.slf4j.LoggerFactory

@Service
@Transactional(readOnly = true)
class SoldeService(
    private val repository: SoldeRepository,
    private val operateurRepository: OperateurRepository,
    private val agenceRepository: AgenceRepository,
) {

    @Transactional
    fun create(@Valid request: SoldeRequest): SoldeResponse {
        val operateur = operateurRepository.findById(request.operateurId)
            .orElseThrow { EntityNotFoundException("Opérateur avec l'id '${request.operateurId}' non trouvé") }

        val agence = agenceRepository.findByCode(request.codeAgence)
            ?: throw EntityNotFoundException("Agence avec le code '${request.codeAgence}' non trouvée")

        val devise = try {
            Devise.valueOf(request.deviseCode.uppercase())
        } catch (e: IllegalArgumentException) {
            throw EntityNotFoundException("Devise '${request.deviseCode}' inconnue")
        }

        ensureUniqueSolde(operateur.id, agence.code, request.soldeType, devise, null)

        val solde = Solde(
            operateur = operateur,
            montant = request.montant,
            soldeType = request.soldeType,
            devise = devise,
            seuilAlerte = request.seuilAlerte,
            misAJourPar = request.misAJourPar,
            agence = agence,
            dernierMiseAJour = System.currentTimeMillis()
        )

        val saved = repository.save(solde)
        return saved.toResponse()
    }

    fun findById(id: Long): SoldeResponse {
        val s = repository.findById(id).orElseThrow { EntityNotFoundException("Solde '$id' non trouvé") }
        return s.toResponse()
    }

    fun findAll(): List<SoldeResponse> =
        repository.findAll().map { it.toResponse() }

    fun findByOperateurId(operateurId: Int): List<SoldeResponse> =
        repository.findByOperateurId(operateurId).map { it.toResponse() }

    fun findByAgenceCode(code: String): List<SoldeResponse> =
        repository.findByAgenceCode(code).map { it.toResponse() }

    fun findBySoldeType(type: SoldeType): List<SoldeResponse> =
        repository.findBySoldeType(type).map { it.toResponse() }

    fun findByDevise(code: String): List<SoldeResponse> {
        val devise = try {
            Devise.valueOf(code.uppercase())
        } catch (e: IllegalArgumentException) {
            throw EntityNotFoundException("Devise '$code' inconnue")
        }
        return repository.findByDevise(devise).map { it.toResponse() }
    }

    @Transactional
    fun update(id: Long, @Valid request: SoldeRequest): SoldeResponse {
        val existing = repository.findById(id).orElseThrow { EntityNotFoundException("Solde '$id' non trouvé") }

        val operateur = operateurRepository.findById(request.operateurId)
            .orElseThrow { EntityNotFoundException("Opérateur avec l'id '${request.operateurId}' non trouvé") }

        val agence = agenceRepository.findByCode(request.codeAgence)
            ?: throw EntityNotFoundException("Agence avec le code '${request.codeAgence}' non trouvée")

        val devise = try {
            Devise.valueOf(request.deviseCode.uppercase())
        } catch (e: IllegalArgumentException) {
            throw EntityNotFoundException("Devise '${request.deviseCode}' inconnue")
        }

        ensureUniqueSolde(operateur.id, agence.code, request.soldeType, devise, existing.id)

        val updated = existing.copy(
            operateur = operateur,
            montant = request.montant,
            soldeType = request.soldeType,
            devise = devise,
            seuilAlerte = request.seuilAlerte,
            misAJourPar = request.misAJourPar,
            agence = agence,
            dernierMiseAJour = System.currentTimeMillis()
        )

        return repository.save(updated).toResponse()
    }

    @Transactional
    fun upsert(operateurId: Int, @Valid request: SoldeRequest): SoldeResponse {
        if (request.operateurId != operateurId) {
            throw IllegalArgumentException("L'identifiant opérateur du chemin et du corps doivent correspondre")
        }

        val operateur = operateurRepository.findById(request.operateurId)
            .orElseThrow { EntityNotFoundException("Opérateur avec l'id '${request.operateurId}' non trouvé") }

        val agence = agenceRepository.findByCode(request.codeAgence)
            ?: throw EntityNotFoundException("Agence avec le code '${request.codeAgence}' non trouvée")

        val devise = try {
            Devise.valueOf(request.deviseCode.uppercase())
        } catch (_: IllegalArgumentException) {
            throw EntityNotFoundException("Devise '${request.deviseCode}' inconnue")
        }

        val matches = repository.findByOperateur_IdAndAgence_CodeAndSoldeTypeAndDevise(
            operateurId = operateur.id,
            agenceCode = agence.code,
            soldeType = request.soldeType,
            devise = devise
        )

        val existing = when {
            matches.isEmpty() -> null
            matches.size == 1 -> matches.first()
            else -> {
                logger.warn(
                    "Plusieurs soldes ({}) trouvés pour operateur={}, agence={}, type={}, devise={}. " +
                        "Le plus récent sera mis à jour.",
                    matches.map { it.id },
                    operateur.id,
                    agence.code,
                    request.soldeType,
                    devise,
                )
                matches.maxByOrNull { it.dernierMiseAJour }
            }
        }

        return if (existing != null) {
            val updated = existing.copy(
                montant = request.montant,
                seuilAlerte = request.seuilAlerte,
                misAJourPar = request.misAJourPar,
                dernierMiseAJour = System.currentTimeMillis()
            )
            repository.save(updated).toResponse()
        } else {
            val solde = Solde(
                operateur = operateur,
                montant = request.montant,
                soldeType = request.soldeType,
                devise = devise,
                seuilAlerte = request.seuilAlerte,
                misAJourPar = request.misAJourPar,
                agence = agence,
                dernierMiseAJour = System.currentTimeMillis()
            )
            repository.save(solde).toResponse()
        }
    }

    @Transactional
    fun delete(id: Long) {
        if (!repository.existsById(id)) {
            throw EntityNotFoundException("Solde '$id' non trouvé")
        }
        repository.deleteById(id)
    }

    private fun ensureUniqueSolde(
        operateurId: Int,
        agenceCode: String,
        soldeType: SoldeType,
        devise: Devise,
        currentId: Long?,
    ) {
        val matches = repository.findByOperateur_IdAndAgence_CodeAndSoldeTypeAndDevise(
            operateurId,
            agenceCode,
            soldeType,
            devise,
        )

        val conflicts = matches.filter { it.id != currentId }
        if (conflicts.isNotEmpty()) {
            if (currentId == null) {
                throw IllegalArgumentException(
                    "Un solde ${soldeType.name.lowercase()} en ${devise.name} existe déjà pour l'opérateur $operateurId et l'agence $agenceCode"
                )
            } else {
                logger.warn(
                    "Duplication détectée lors de la mise à jour du solde {} : autres enregistrements présents {}",
                    currentId,
                    conflicts.map { it.id }
                )
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SoldeService::class.java)
    }
}



