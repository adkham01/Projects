package com.system.payment.student_payment_system.services;

import com.system.payment.student_payment_system.database.dto.TransactionUpdateDto;
import com.system.payment.student_payment_system.database.models.Transaction;
import com.system.payment.student_payment_system.database.models.report.SummaryReport;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface InterfaceTransactionService {
    Transaction createNewTransaction(String studentId, Transaction transaction);
    Transaction getTransactionById(String id);
    Page<Transaction> getTransactionsByPagination(LocalDate from, LocalDate to, Double minAmount, int page, int size);
    Transaction updateTransactionById(String id, TransactionUpdateDto dto);
    void deleteTransactionById(String id);
    SummaryReport getReportsSummary(LocalDate from, LocalDate to);
}
