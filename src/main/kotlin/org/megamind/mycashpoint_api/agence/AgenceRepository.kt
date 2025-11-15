package org.megamind.mycashpoint_api.agence

import org.springframework.data.jpa.repository.JpaRepository

interface AgenceRepository : JpaRepository<Agence, String> {
    fun findByCode(code: String): Agence?
}