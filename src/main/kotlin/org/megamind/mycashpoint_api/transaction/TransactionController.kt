package org.megamind.mycashpoint_api.transaction

import TransactionType
import jakarta.validation.Valid
import org.megamind.mycashpoint_api.transaction.dto.TransactionRequest
import org.megamind.mycashpoint_api.transaction.dto.TransactionResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transaction")
class TransactionController(
    private val service: TransactionService
) {

    @PostMapping
    fun create(@Valid @RequestBody request: TransactionRequest): ResponseEntity<TransactionResponse> {
        val created = service.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PostMapping("/batch")
    fun createBatch(
        @Valid @RequestBody requests: List<@Valid TransactionRequest>
    ): ResponseEntity<List<TransactionResponse>> {
        val created = service.createAll(requests)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<TransactionResponse> =
        ResponseEntity.ok(service.findById(id))

    @GetMapping
    fun findAll(
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<TransactionResponse>> =
        ResponseEntity.ok(service.findAll(pageable))

    @GetMapping("/operateur/{operateurId}")
    fun findByOperateur(@PathVariable operateurId: Int): ResponseEntity<List<TransactionResponse>> =
        ResponseEntity.ok(service.findByOperateurId(operateurId))

    @GetMapping("/agence/{code}")
    fun findByAgence(
        @PathVariable code: String,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<TransactionResponse>> =
        ResponseEntity.ok(service.findByAgenceCode(code, pageable))

    @GetMapping("/type/{type}")
    fun findByType(@PathVariable type: TransactionType): ResponseEntity<List<TransactionResponse>> =
        ResponseEntity.ok(service.findByType(type))

    @GetMapping("/periode")
    fun findByPeriode(
        @RequestParam start: Long,
        @RequestParam end: Long
    ): ResponseEntity<List<TransactionResponse>> =
        ResponseEntity.ok(service.findByPeriode(start, end))

    @GetMapping("/code/{code}")
    fun findByCode(@PathVariable code: String): ResponseEntity<TransactionResponse> =
        ResponseEntity.ok(service.findByCode(code))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}


