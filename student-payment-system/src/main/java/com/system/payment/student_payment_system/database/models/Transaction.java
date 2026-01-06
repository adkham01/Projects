package com.system.payment.student_payment_system.database.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AttributeOverride(
        name = "identifier",
        column = @Column(name = "transaction_id")
)
public class Transaction extends BaseEntity {

    @Positive(message = "Amount must be greater than 0")
    @Column(nullable = false, updatable = false)
    private double amount;

    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private final LocalDate date = LocalDate.now();

    @Size(max = 255)
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, updatable = false)
    @JsonIgnore
    private Student student;

    public Transaction(double amount, String description, Student student) {
        this.amount = amount;
        this.description = description;
        this.student = student;
    }

    public void updateDescription(String description) {
        this.description = description;
    }


}