package org.megamind.mycashpoint_api.user

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class CustomUserDetailsService(
    private val userRepository: UserRespository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user =
            userRepository.findByUserName(username)
                ?: throw UsernameNotFoundException("Utilisateur non trouvé")


        return User
            .withUsername(user.userName)
            .password(user.password)
            .roles(user.role.name)
            .build()
    }
}