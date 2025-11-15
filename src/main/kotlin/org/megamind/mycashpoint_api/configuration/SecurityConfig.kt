package org.megamind.mycashpoint_api.configuration

import org.megamind.mycashpoint_api.jwt.JwtAuthenticationFilter
import org.megamind.mycashpoint_api.jwt.JwtService
import org.megamind.mycashpoint_api.user.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtService: JwtService,
    private val userDetailsService: CustomUserDetailsService
) {


    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }.cors { }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/auth/**").permitAll()
                auth.requestMatchers("/agence/**").permitAll()
                auth.requestMatchers("/operateur/**").permitAll()
                auth.requestMatchers("/commission/**").permitAll()
                auth.requestMatchers("/devise/**").permitAll()
                    .anyRequest().authenticated()
            }.addFilterBefore(
                JwtAuthenticationFilter(jwtService, userDetailsService),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }


}