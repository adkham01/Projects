package com.system.payment.student_payment_system.database.repository;

import com.system.payment.student_payment_system.database.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByDateBetween(LocalDate from, LocalDate to);

    Page<Transaction> findByDateBetweenAndAmountGreaterThanEqual(LocalDate from, LocalDate to, double amount, Pageable pageable);


}
