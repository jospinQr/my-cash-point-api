package org.megamind.mycashpoint_api.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.id.factory.spi.GenerationTypeStrategy
import org.megamind.mycashpoint_api.agence.Agence

@Entity
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val userName: String,
    @Column(nullable = false, unique = true)
    val password: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_code", nullable = true)
    val agence: Agence?,
    @Enumerated(EnumType.STRING)
    val role: Role
)

enum class Role {
    ADMIN,
    AGENT
}