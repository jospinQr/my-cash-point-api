package org.megamind.mycashpoint_api.user

import org.megamind.mycashpoint_api.user.dto.AuthResponse
import org.megamind.mycashpoint_api.user.dto.LoginRequest
import org.megamind.mycashpoint_api.user.dto.RegisterRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val service: AuthService) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {

        val authResponse = service.login(loginRequest)
        return ResponseEntity.ok(authResponse)

    }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<AuthResponse> {

        val authResponse = service.register(registerRequest)
        return ResponseEntity.ok(authResponse)

    }

}