package org.megamind.mycashpoint_api.transaction.dto

import TransactionType
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

import java.math.BigDecimal

data class TransactionRequest(
    @field:NotNull
    val operateurId: Int,

    @field:NotNull
    val type: TransactionType,

    @field:NotNull @field:Min(0)
    val montant: BigDecimal,

    // Optionnel: si non fourni, le serveur génère un code unique
    val transactionCode: String? = null,

    val commission: Float? = null,
    val nomClient: String? = null,
    val numClient: String? = null,
    val nomBeneficaire: String? = null,
    val numBeneficaire: String? = null,

    val soldeAvant: BigDecimal? = null,
    val soldeApres: BigDecimal? = null,

    @field:NotBlank
    val deviseCode: String, // USD | CDF (mapped to enum)

    val reference: String? = null,
    val note: String? = null,

    @field:NotBlank
    val codeAgence: String,

    @field:NotNull
    val creePar: Long,
)


