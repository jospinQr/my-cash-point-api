package org.megamind.mycashpoint_api.solde

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SoldeRepository : JpaRepository<Solde, Long> {
    fun findByOperateurId(operateurId: Int): List<Solde>
    fun findBySoldeType(soldeType: SoldeType): List<Solde>
    fun findByDevise(devise: Devise): List<Solde>

    @Query("select s from Solde s where s.agence.code = :code")
    fun findByAgenceCode(@Param("code") code: String): List<Solde>

    fun findByOperateur_IdAndAgence_CodeAndSoldeTypeAndDevise(
        operateurId: Int,
        agenceCode: String,
        soldeType: SoldeType,
        devise: Devise,
    ): List<Solde>
}



