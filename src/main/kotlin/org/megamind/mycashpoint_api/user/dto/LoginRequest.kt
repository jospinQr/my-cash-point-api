package org.megamind.mycashpoint_api.user.dto

import jakarta.validation.constraints.NotBlank


data class LoginRequest(

    val username: String,
    val password: String,
)

data class AuthResponse(val token: String)