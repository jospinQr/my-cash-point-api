package org.megamind.mycashpoint_api.transaction

import TransactionType
import jakarta.persistence.*
import org.megamind.mycashpoint_api.agence.Agence
import org.megamind.mycashpoint_api.operateur.Operateur
import org.megamind.mycashpoint_api.solde.Devise
import java.math.BigDecimal

@Entity
@Table(name = "transactions")
data class Transaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operateur_id", nullable = false)
    val operateur: Operateur,

    @Column(nullable = false, unique = true, length = 64)
    val transactionCode: String,

    @Column(name = "commision")
    val commission: Float? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    val type: TransactionType = TransactionType.DEPOT,

    @Column(nullable = false, precision = 19, scale = 4)
    val montant: BigDecimal = BigDecimal.ZERO,

    @Column
    val nomClient: String? = null,
    @Column
    val numClient: String? = null,

    @Column
    val nomBeneficaire: String? = null,
    @Column
    val numBeneficaire: String? = null,

    @Column(precision = 19, scale = 4)
    val soldeAvant: BigDecimal? = null,
    @Column(precision = 19, scale = 4)
    val soldeApres: BigDecimal? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    val devise: Devise,

    @Column
    val reference: String? = null,
    @Column(columnDefinition = "TEXT")
    val note: String? = null,

    @Column(nullable = false)
    val horodatage: Long = System.currentTimeMillis(),

    @Column(nullable = false)
    val creePar: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_code", nullable = false)
    val agence: Agence,


    )



