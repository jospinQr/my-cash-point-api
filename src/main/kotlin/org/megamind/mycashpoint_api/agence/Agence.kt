package org.megamind.mycashpoint_api.agence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity()
@Table(name = "agences")
data class Agence(
    @Id val code: String,
    val name: String
)