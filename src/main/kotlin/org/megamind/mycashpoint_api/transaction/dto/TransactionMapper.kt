package org.megamind.mycashpoint_api.transaction.dto

import org.megamind.mycashpoint_api.transaction.Transaction

fun Transaction.toResponse(): TransactionResponse =
    TransactionResponse(
        id = id,
        transactionCode = transactionCode,
        operateurId = operateur.id,
        operateurName = operateur.name,
        type = type,
        montant = montant,
        commission = commission,
        nomClient = nomClient,
        numClient = numClient,
        nomBeneficaire = nomBeneficaire,
        numBeneficaire = numBeneficaire,
        soldeAvant = soldeAvant,
        soldeApres = soldeApres,
        devise = devise,
        reference = reference,
        note = note,
        horodatage = horodatage,
        creePar = creePar,
        codeAgence = agence.code,

        )


