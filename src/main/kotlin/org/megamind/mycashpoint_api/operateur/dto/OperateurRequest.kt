package org.megamind.mycashpoint_api.operateur.dto

import jakarta.validation.constraints.NotEmpty

data class OperateurRequest(
    @field:NotEmpty
    val name: String
)

