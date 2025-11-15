package org.megamind.mycashpoint_api.solde.dto

import org.megamind.mycashpoint_api.solde.Devise
import org.megamind.mycashpoint_api.solde.SoldeType
import java.math.BigDecimal

data class SoldeResponse(
    val id: Long,
    val operateurId: Int,
    val operateurName: String,
    val soldeType: SoldeType,
    val montant: BigDecimal,
    val devise: Devise,
    val dernierMiseAJour: Long,
    val seuilAlerte: Double?,
    val misAJourPar: Long,
    val codeAgence: String,
)




