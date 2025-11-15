package org.megamind.mycashpoint_api.solde

import jakarta.persistence.*
import jakarta.persistence.UniqueConstraint
import org.megamind.mycashpoint_api.agence.Agence
import org.megamind.mycashpoint_api.operateur.Operateur
import java.math.BigDecimal

@Entity
@Table(
    name = "soldes",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_solde_operateur_agence_type_devise",
            columnNames = ["operateur_id", "agence_code", "solde_type", "devise"]
        )
    ]
)
data class Solde(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operateur_id", nullable = false)
    val operateur: Operateur,

    @Column(nullable = false, precision = 19, scale = 4)
    val montant: BigDecimal = BigDecimal.ZERO,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    val soldeType: SoldeType = SoldeType.PHYSIQUE,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    val devise: Devise = Devise.USD,

    @Column(nullable = false)
    val dernierMiseAJour: Long,

    @Column
    val seuilAlerte: Double? = null,

    @Column(nullable = false)
    val misAJourPar: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_code", nullable = false)
    val agence: Agence,
)

enum class Devise{
    USD,
    CDF
}



