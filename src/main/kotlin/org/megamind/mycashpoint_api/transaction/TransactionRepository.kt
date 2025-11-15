package org.megamind.mycashpoint_api.transaction

import TransactionType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByTransactionCode(code: String): Transaction?
    fun findByOperateurId(operateurId: Int): List<Transaction>

    @Query(
        value = "select t from Transaction t where t.agence.code = :code",
        countQuery = "select count(t) from Transaction t where t.agence.code = :code"
    )
    fun findByAgenceCode(@Param("code") code: String, pageable: Pageable): Page<Transaction>

    fun findByType(type: TransactionType): List<Transaction>

    @Query("select t from Transaction t where t.horodatage between :start and :end")
    fun findByHorodatageBetween(@Param("start") start: Long, @Param("end") end: Long): List<Transaction>
}



