package com.system.payment.student_payment_system.services;

import com.system.payment.student_payment_system.database.dto.TransactionUpdateDto;
import com.system.payment.student_payment_system.database.models.Student;
import com.system.payment.student_payment_system.database.models.Transaction;
import com.system.payment.student_payment_system.database.models.report.SummaryReport;
import com.system.payment.student_payment_system.database.repository.StudentRepository;
import com.system.payment.student_payment_system.database.repository.TransactionRepository;
import com.system.payment.student_payment_system.exception.StudentNotFoundException;
import com.system.payment.student_payment_system.exception.TransactionNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService implements InterfaceTransactionService {

    private final TransactionRepository repository;
    private final StudentRepository studentRepository;

    public Transaction createNewTransaction(String studentId, Transaction transaction) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));

        Transaction newTransaction = new Transaction(
                transaction.getAmount(),
                transaction.getDescription(),
                student
        );

        return repository.save(newTransaction);
    }

    public Transaction getTransactionById(String id) {
        return getTransactionFromRepo(id);
    }

    public Page<Transaction> getTransactionsByPagination(LocalDate from, LocalDate to, Double minAmount, int page, int size) {
        PaginationCreator pagination = new PaginationCreator();
        Pageable pageable = pagination.getPageable(page, size);

        LocalDate startDate = Optional.ofNullable(from).orElse(pagination.getFrom());
        LocalDate endDate = Optional.ofNullable(to).orElse(pagination.getTo());
        double minAmountValue = Optional.ofNullable(minAmount).orElse(pagination.getMinAmount());
        return repository.findByDateBetweenAndAmountGreaterThanEqual(startDate, endDate, minAmountValue, pageable);
    }

    @Transactional
    public Transaction updateTransactionById(String id, @Valid TransactionUpdateDto dto) {
        Transaction transaction = getTransactionFromRepo(id);
        Optional.ofNullable(dto.description()).ifPresent(transaction::updateDescription);
        Optional.of(dto.amount()).ifPresent(transaction::setAmount);
        return repository.save(transaction);
    }

    public void deleteTransactionById(String id) {
        repository.delete(getTransactionFromRepo(id));
    }

    private Transaction getTransactionFromRepo(String id) {
        return repository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));

    }

    public SummaryReport getReportsSummary(LocalDate from, LocalDate to) {

        LocalDate fromDate = Optional.ofNullable(from).orElse(LocalDate.now().minusDays(7));
        LocalDate toDate = Optional.ofNullable(to).orElse(LocalDate.now());

        double amount = repository.findByDateBetween(fromDate, toDate).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        int transactions = repository.findByDateBetween(fromDate, toDate).size();

        return new SummaryReport(amount, transactions, fromDate, toDate);

    }
}
