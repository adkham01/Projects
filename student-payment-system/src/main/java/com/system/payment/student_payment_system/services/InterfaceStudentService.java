package com.system.payment.student_payment_system.services;

import com.system.payment.student_payment_system.database.dto.StudentUpdateDto;
import com.system.payment.student_payment_system.database.models.Student;
import org.springframework.data.domain.Page;

public interface InterfaceStudentService {
    Student createStudent(Student student);
    Page<?> getStudentsByPagination(int page, int size);
    Student getStudentById(String studentId);
    Student updateStudentById(String studentId, StudentUpdateDto dto);
    void deleteById(String studentId);
}
