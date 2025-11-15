package org.megamind.mycashpoint_api.user.dto

import org.megamind.mycashpoint_api.user.Role

data class RegisterRequest(
    val username: String,
    val password: String,
    val codeAgence: String?,
    val role: Role,
)

