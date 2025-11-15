package org.megamind.mycashpoint_api.user

import jakarta.validation.Valid
import org.megamind.mycashpoint_api.agence.AgenceRepository
import org.megamind.mycashpoint_api.jwt.JwtService
import org.megamind.mycashpoint_api.user.dto.AuthResponse
import org.megamind.mycashpoint_api.user.dto.LoginRequest
import org.megamind.mycashpoint_api.user.dto.RegisterRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRespository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val agenceRepository: AgenceRepository
) {


    fun login(@Valid request: LoginRequest): AuthResponse {

        val user =
            userRepository.findByUserName(request.username) ?: throw IllegalArgumentException("Ce nom n'exixte pas")


        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Mot de passe incorrect")
        }

        val token =
            jwtService.generateToken(user.id, user.userName, role = user.role.name, codeAgence = user.agence?.code)

        return AuthResponse(token)
    }


    fun register(@Valid request: RegisterRequest): AuthResponse {

        if (userRepository.findByUserName(request.username) != null) {
            throw IllegalArgumentException("Ce nom d'utilisateur existe déjà")
        }

        if (request.role == Role.AGENT && request.codeAgence == null) {
            throw IllegalArgumentException("L'agence est obligatoire pour un agent")
        }

        val agence = if (request.codeAgence != null) {
            agenceRepository.findByCode(request.codeAgence) ?: throw IllegalArgumentException("Agence non trouvé")
        } else {
            null
        }

        val user = User(
            userName = request.username,
            password = passwordEncoder.encode(request.password),
            role = request.role,
            agence = agence
        )

        val savedUser = userRepository.save(user)
        val token = jwtService.generateToken(
            user.id,
            savedUser.userName,
            role = savedUser.role.name,
            codeAgence = savedUser.agence?.code
        )

        return AuthResponse(token)
    }

}