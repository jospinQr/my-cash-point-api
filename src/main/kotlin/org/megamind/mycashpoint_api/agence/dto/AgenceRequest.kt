package org.megamind.mycashpoint_api.agence.dto

import jakarta.validation.constraints.NotEmpty

data class AgenceRequest(
    @field:NotEmpty
    val code: String,
    @field:NotEmpty
    val name: String
)