package org.megamind.mycashpoint_api.operateur

import jakarta.validation.Valid
import org.megamind.mycashpoint_api.operateur.dto.OperateurRequest
import org.megamind.mycashpoint_api.operateur.dto.OperateurResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/operateur")
class OperateurController(
    private val service: OperateurService
) {

    @PostMapping
    fun create(
        @Valid @RequestBody request: OperateurRequest
    ): ResponseEntity<OperateurResponse> {
        val operateur = service.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(operateur)
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: Int
    ): ResponseEntity<OperateurResponse> {
        val operateur = service.findById(id)
        return ResponseEntity.ok(operateur)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<OperateurResponse>> {
        val operateurs = service.findAll()
        return ResponseEntity.ok(operateurs)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @Valid @RequestBody request: OperateurRequest
    ): ResponseEntity<OperateurResponse> {
        val operateur = service.update(id, request)
        return ResponseEntity.ok(operateur)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Int
    ): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}

