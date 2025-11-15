package org.megamind.mycashpoint_api.operateur

import org.springframework.data.jpa.repository.JpaRepository

interface OperateurRepository : JpaRepository<Operateur, Int> {
    fun findByName(name: String): Operateur?
}

