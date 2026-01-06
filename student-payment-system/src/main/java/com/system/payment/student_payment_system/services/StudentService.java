package com.system.payment.student_payment_system.services;

import com.system.payment.student_payment_system.database.dto.StudentUpdateDto;
import com.system.payment.student_payment_system.database.models.Student;
import com.system.payment.student_payment_system.database.models.Transaction;
import com.system.payment.student_payment_system.database.repository.StudentRepository;
import com.system.payment.student_payment_system.exception.StudentNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class StudentService implements InterfaceStudentService {
    private final StudentRepository studentRepository;

    public Student createStudent(@Valid Student student) {
        Set<Transaction> transactions = Optional.ofNullable(student.getTransactions())
                .orElseGet(HashSet::new);

        transactions.forEach(transaction -> transaction.setStudent(student));
        student.setTransactions(transactions);

        return studentRepository.save(student);
    }

    public Page<?> getStudentsByPagination(int page, int size) {
        PageRequest pageRequest = PageRequest.ofSize(size).withPage(page);
        return studentRepository.findAll(pageRequest);
    }

    public Student getStudentById(String studentId) {
        return getStudentFromRepo(studentId);
    }


    @Transactional
    public Student updateStudentById(String studentId, StudentUpdateDto dto) {
        Student student = getStudentFromRepo(studentId);

        Optional.ofNullable(dto.firstName()).ifPresent(student::setFirstName);
        Optional.ofNullable(dto.lastName()).ifPresent(student::setLastName);
        Optional.ofNullable(dto.phoneNumber()).ifPresent(student::setPhoneNumber);
        Optional.ofNullable(dto.yearOfStudy()).ifPresent(student::setYearOfStudy);
        Optional.ofNullable(dto.groupNumber()).ifPresent(student::setGroupNumber);
        Optional.ofNullable(dto.fieldOfStudy()).ifPresent(student::setFieldOfStudy);
        Optional.ofNullable(dto.transactions()).ifPresent(student::setTransactions);

        return student;
    }

    public void deleteById(String studentId) {
        studentRepository.delete(getStudentFromRepo(studentId));
    }

    private Student getStudentFromRepo(String studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + studentId));
    }
}
