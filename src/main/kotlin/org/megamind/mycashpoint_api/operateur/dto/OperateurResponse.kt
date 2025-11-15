package org.megamind.mycashpoint_api.operateur.dto

import org.megamind.mycashpoint_api.operateur.Operateur

data class OperateurResponse(
    val id: Int,
    val name: String
)

fun Operateur.toResponse(): OperateurResponse = OperateurResponse(
    id = this.id,
    name = this.name
)

