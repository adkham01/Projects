package com.system.payment.student_payment_system.database.dto;

import com.system.payment.student_payment_system.database.models.Transaction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record StudentUpdateDto(
        @Size(min = 2, max = 45, message = "First name must be 2-45 characters")
        String firstName,

        @Size(min = 2, max = 45, message = "Last name must be 2-45 characters")
        String lastName,

        @Pattern(regexp = "^[+]?[0-9\\s\\-]{10,15}$", message = "Invalid phone number")
        String phoneNumber,

        @Min(1) @Max(5)
        Integer yearOfStudy,

        @Min(1) @Max(5999)
        Integer groupNumber,

        String fieldOfStudy,

        Set<Transaction> transactions
) {
}
