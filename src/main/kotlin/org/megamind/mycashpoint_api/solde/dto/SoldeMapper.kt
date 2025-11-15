package org.megamind.mycashpoint_api.solde.dto

import org.megamind.mycashpoint_api.solde.Solde

fun Solde.toResponse(): SoldeResponse =
    SoldeResponse(
        id = id,
        operateurId = operateur.id,
        operateurName = operateur.name,
        soldeType = soldeType,
        montant = montant,
        devise = devise,
        dernierMiseAJour = dernierMiseAJour,
        seuilAlerte = seuilAlerte,
        misAJourPar = misAJourPar,
        codeAgence = agence.code
    )




