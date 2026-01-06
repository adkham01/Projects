package com.system.payment.student_payment_system.database.repository;

import com.system.payment.student_payment_system.database.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
}
