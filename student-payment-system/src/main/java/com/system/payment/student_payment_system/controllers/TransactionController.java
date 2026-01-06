package com.system.payment.student_payment_system.controllers;

import com.system.payment.student_payment_system.database.dto.TransactionUpdateDto;
import com.system.payment.student_payment_system.database.models.Transaction;
import com.system.payment.student_payment_system.services.InterfaceTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {
    private final InterfaceTransactionService service;

    @PostMapping("/students/{studentId}/transactions")
    public ResponseEntity<?> createNewTransaction(
            @PathVariable String studentId,
            @Valid @RequestBody Transaction transaction) {

        Transaction savedTransaction = service.createNewTransaction(studentId, transaction);

        URI location = linkTo(methodOn(TransactionController.class)
                .getTransactionById(savedTransaction.getIdentifier()))
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactionsByPagination(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {


        return ResponseEntity.ok(service.getTransactionsByPagination(from, to, minAmount, page, size));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable String id) {
        return ResponseEntity.ok().body(service.getTransactionById(id));
    }

    @PutMapping("/transactions/{id}")
    public ResponseEntity<?> updateTransactionById(@PathVariable String id,
                                                   @Valid @RequestBody TransactionUpdateDto dto) {
        Transaction transaction = service.updateTransactionById(id, dto);
        return ResponseEntity.ok(transaction);
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<?> deleteTransactionById(@PathVariable String id) {
        service.deleteTransactionById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reports/summary")
    public ResponseEntity<?> getReportsSummary(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to ){
        return ResponseEntity.ok(service.getReportsSummary(from, to));
    }

}
