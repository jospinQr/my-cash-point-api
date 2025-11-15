package org.megamind.mycashpoint_api.agence

import jakarta.validation.Valid
import org.megamind.mycashpoint_api.agence.dto.AgenceRequest
import org.megamind.mycashpoint_api.agence.dto.AgenceResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/agence") // ✅ Préfixe /api pour les REST API
class AgenceController(
    private val service: AgenceService
) {

    /**
     * POST /api/agences
     * Créer une nouvelle agence
     */
    @PostMapping
    fun create(
        @Valid @RequestBody request: AgenceRequest
    ): ResponseEntity<AgenceResponse> {
        val agence = service.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(agence) // 201
    }

    /**
     * GET /api/agences/{code}
     * Récupérer une agence par son code
     */
    @GetMapping("/{code}")
    fun findByCode(
        @PathVariable code: String
    ): ResponseEntity<AgenceResponse> {
        val agence = service.findByCode(code)
        return ResponseEntity.ok(agence) // 200
    }

    /**
     * GET /api/agences
     * Récupérer toutes les agences
     */
    @GetMapping
    fun findAll(): ResponseEntity<List<AgenceResponse>> {
        val agences = service.findAll()
        return ResponseEntity.ok(agences) // 200
    }

    /**
     * PUT /api/agences/{code}
     * Mettre à jour une agence
     */
    @PutMapping("/{code}")
    fun update(
        @PathVariable code: String,
        @Valid @RequestBody request: AgenceRequest
    ): ResponseEntity<AgenceResponse> {
        val agence = service.update(code, request)
        return ResponseEntity.ok(agence) // 200
    }

    /**
     * DELETE /api/agences/{code}
     * Supprimer une agence
     */
    @DeleteMapping("/{code}")
    fun delete(
        @PathVariable code: String
    ): ResponseEntity<Void> {
        service.delete(code)
        return ResponseEntity.noContent().build() // 204
    }


}