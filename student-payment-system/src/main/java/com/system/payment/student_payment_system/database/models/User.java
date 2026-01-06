package com.system.payment.student_payment_system.database.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(
        name = "identifier",
        column = @Column(name = "user_id")
)
public class User extends BaseEntity {
    private String username;
    private String password;
    private String role;
}
