package org.megamind.mycashpoint_api.agence.dto

import org.megamind.mycashpoint_api.agence.Agence

data class AgenceResponse(
    val code: String,
    val name: String,
)


fun Agence.toResponse(): AgenceResponse = AgenceResponse(
    code = this.code,
    name = this.name
)

