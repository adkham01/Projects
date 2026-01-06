package com.system.payment.student_payment_system.database.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TransactionUpdateDto(
        @Positive(message = "Amount must be greater than 0")
        double amount,
        @Size(max = 255, message = "Description must be less than 255 characters")
        String description
) {
}