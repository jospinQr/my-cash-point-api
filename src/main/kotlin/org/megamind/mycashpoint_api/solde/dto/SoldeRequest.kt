package org.megamind.mycashpoint_api.solde.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.megamind.mycashpoint_api.solde.SoldeType
import java.math.BigDecimal

data class SoldeRequest(
    @field:NotNull
    val operateurId: Int,

    @field:NotNull
    val soldeType: SoldeType,

    @field:NotNull @field:Min(0)
    val montant: BigDecimal,

    @field:NotBlank
    val deviseCode: String,

    val seuilAlerte: Double? = null,

    @field:NotNull
    val misAJourPar: Long,

    @field:NotNull
    val dernierMiseAJour: Long,

    @field:NotBlank
    val codeAgence: String,
)




