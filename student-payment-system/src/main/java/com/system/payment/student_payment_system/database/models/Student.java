package com.system.payment.student_payment_system.database.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(
        name = "identifier",
        column = @Column(name = "student_id")
)
public class Student extends BaseEntity {

    @Size(min = 2, max = 45, message = "First name must be 2-45 characters")
    private String firstName;

    @Size(min = 2, max = 45, message = "Last name must be 2-45 characters")
    private String lastName;

    @Pattern(regexp = "^[+]?[0-9\\s\\-]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @Min(1) @Max(5)
    private Integer yearOfStudy;

    @Min(1) @Max(5999)
    private Integer groupNumber;

    private String fieldOfStudy;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Transaction> transactions;
}
