package org.megamind.mycashpoint_api.transaction

import TransactionType
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.megamind.mycashpoint_api.agence.AgenceRepository

import org.megamind.mycashpoint_api.operateur.OperateurRepository
import org.megamind.mycashpoint_api.solde.Devise
import org.megamind.mycashpoint_api.transaction.dto.TransactionRequest
import org.megamind.mycashpoint_api.transaction.dto.TransactionResponse
import org.megamind.mycashpoint_api.transaction.dto.toResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class TransactionService(
    private val repository: TransactionRepository,
    private val operateurRepository: OperateurRepository,
    private val agenceRepository: AgenceRepository,
) {

    @Transactional
    fun create(@Valid request: TransactionRequest): TransactionResponse {
        val transaction = buildTransaction(request, inBatchCodes = null)
        val saved = repository.save(transaction)
        return saved.toResponse()
    }

    fun findById(id: Long): TransactionResponse {
        val tx = repository.findById(id).orElseThrow { EntityNotFoundException("Transaction '$id' non trouvée") }
        return tx.toResponse()
    }

    fun findAll(pageable: Pageable): Page<TransactionResponse> =
        repository.findAll(pageable).map { it.toResponse() }

    fun findByOperateurId(operateurId: Int): List<TransactionResponse> =
        repository.findByOperateurId(operateurId).map { it.toResponse() }

    fun findByAgenceCode(code: String, pageable: Pageable): Page<TransactionResponse> =
        repository.findByAgenceCode(code, pageable).map { it.toResponse() }

    fun findByType(type: TransactionType): List<TransactionResponse> =
        repository.findByType(type).map { it.toResponse() }

    fun findByPeriode(start: Long, end: Long): List<TransactionResponse> =
        repository.findByHorodatageBetween(start, end).map { it.toResponse() }

    @Transactional
    fun createAll(@Valid requests: List<@Valid TransactionRequest>): List<TransactionResponse> {
        if (requests.isEmpty()) {
            return emptyList()
        }

        val inBatchCodes = mutableSetOf<String>()

        val transactions = requests.map { request ->
            buildTransaction(request, inBatchCodes)
        }

        val saved = repository.saveAll(transactions)
        return saved.map { it.toResponse() }
    }

    @Transactional
    fun delete(id: Long) {
        if (!repository.existsById(id)) {
            throw EntityNotFoundException("Transaction '$id' non trouvée")
        }
        repository.deleteById(id)
    }

    fun findByCode(code: String): TransactionResponse {
        val tx = repository.findByTransactionCode(code)
            ?: throw EntityNotFoundException("Transaction avec le code '$code' non trouvée")
        return tx.toResponse()
    }

    private fun buildTransaction(
        request: TransactionRequest,
        inBatchCodes: MutableSet<String>?,
    ): Transaction {
        val operateur = operateurRepository.findById(request.operateurId)
            .orElseThrow { EntityNotFoundException("Opérateur avec l'id '${request.operateurId}' non trouvé") }

        val agence = agenceRepository.findByCode(request.codeAgence)
            ?: throw EntityNotFoundException("Agence avec le code '${request.codeAgence}' non trouvée")

        val code = resolveTransactionCode(request.transactionCode, inBatchCodes)
        val devise = resolveDevise(request.deviseCode)

        return Transaction(
            operateur = operateur,
            transactionCode = code,
            commission = request.commission,
            type = request.type,
            montant = request.montant,
            nomClient = request.nomClient,
            numClient = request.numClient,
            nomBeneficaire = request.nomBeneficaire,
            numBeneficaire = request.numBeneficaire,
            soldeAvant = request.soldeAvant,
            soldeApres = request.soldeApres,
            devise = devise,
            reference = request.reference,
            note = request.note,
            creePar = request.creePar,
            agence = agence,
        )
    }

    private fun resolveDevise(code: String): Devise =
        try {
            Devise.valueOf(code.uppercase())
        } catch (e: IllegalArgumentException) {
            throw EntityNotFoundException("Devise '$code' inconnue")
        }

    private fun resolveTransactionCode(
        requestedCode: String?,
        inBatchCodes: MutableSet<String>?,
    ): String {
        if (!requestedCode.isNullOrBlank()) {
            val normalized = requestedCode.trim()
            if (inBatchCodes?.contains(normalized) == true) {
                throw IllegalArgumentException("Le code de transaction '$normalized' apparaît plusieurs fois dans le lot")
            }
            if (repository.findByTransactionCode(normalized) != null) {
                throw IllegalArgumentException("Le code de transaction '$normalized' existe déjà")
            }
            inBatchCodes?.add(normalized)
            return normalized
        }

        var generated: String
        do {
            generated = generateTransactionCode()
        } while (
            repository.findByTransactionCode(generated) != null ||
            (inBatchCodes != null && generated in inBatchCodes)
        )

        inBatchCodes?.add(generated)
        return generated
    }

    private fun generateTransactionCode(): String =
        UUID.randomUUID().toString().replace("-", "").uppercase().take(20)
}


