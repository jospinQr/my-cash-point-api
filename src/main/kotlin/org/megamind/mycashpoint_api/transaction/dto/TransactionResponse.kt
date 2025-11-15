package org.megamind.mycashpoint_api.transaction.dto


import TransactionType
import org.megamind.mycashpoint_api.solde.Devise
import org.megamind.mycashpoint_api.transaction.StatutSync

import java.math.BigDecimal

data class TransactionResponse(
    val id: Long,
    val transactionCode: String,
    val operateurId: Int,
    val operateurName: String,
    val type: TransactionType,
    val montant: BigDecimal,
    val commission: Float?,
    val nomClient: String?,
    val numClient: String?,
    val nomBeneficaire: String?,
    val numBeneficaire: String?,
    val soldeAvant: BigDecimal?,
    val soldeApres: BigDecimal?,
    val devise: Devise,
    val reference: String?,
    val note: String?,
    val horodatage: Long,
    val creePar: Long,
    val codeAgence: String,

)




