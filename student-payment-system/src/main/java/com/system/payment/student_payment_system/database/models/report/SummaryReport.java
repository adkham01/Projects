package com.system.payment.student_payment_system.database.models.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class SummaryReport {
    private double totalAmount;
    private int totalTransactions;
    private LocalDate from;
    private LocalDate to;
}
