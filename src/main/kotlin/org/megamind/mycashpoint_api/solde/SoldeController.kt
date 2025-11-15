package org.megamind.mycashpoint_api.solde

import jakarta.validation.Valid
import org.megamind.mycashpoint_api.solde.dto.SoldeRequest
import org.megamind.mycashpoint_api.solde.dto.SoldeResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/solde")
class SoldeController(
    private val service: SoldeService
) {

    @PostMapping
    fun create(@Valid @RequestBody request: SoldeRequest): ResponseEntity<SoldeResponse> {
        val created = service.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<SoldeResponse> =
        ResponseEntity.ok(service.findById(id))

    @GetMapping
    fun findAll(): ResponseEntity<List<SoldeResponse>> =
        ResponseEntity.ok(service.findAll())

    @GetMapping("/operateur/{operateurId}")
    fun findByOperateur(@PathVariable operateurId: Int): ResponseEntity<List<SoldeResponse>> =
        ResponseEntity.ok(service.findByOperateurId(operateurId))

    @GetMapping("/agence/{code}")
    fun findByAgence(@PathVariable code: String): ResponseEntity<List<SoldeResponse>> =
        ResponseEntity.ok(service.findByAgenceCode(code))

    @GetMapping("/type/{type}")
    fun findByType(@PathVariable type: SoldeType): ResponseEntity<List<SoldeResponse>> =
        ResponseEntity.ok(service.findBySoldeType(type))

    @GetMapping("/devise/{code}")
    fun findByDevise(@PathVariable code: String): ResponseEntity<List<SoldeResponse>> =
        ResponseEntity.ok(service.findByDevise(code))

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: SoldeRequest): ResponseEntity<SoldeResponse> =
        ResponseEntity.ok(service.update(id, request))

    @PutMapping("/operateur/{operateurId}")
    fun upsert(
        @PathVariable operateurId: Int,
        @Valid @RequestBody request: SoldeRequest
    ): ResponseEntity<SoldeResponse> =
        ResponseEntity.ok(service.upsert(operateurId, request))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}



