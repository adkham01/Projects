package com.system.payment.student_payment_system.controllers;

import com.system.payment.student_payment_system.database.dto.StudentUpdateDto;
import com.system.payment.student_payment_system.database.models.Student;
import com.system.payment.student_payment_system.database.models.Transaction;
import com.system.payment.student_payment_system.services.InterfaceStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController{

    private final InterfaceStudentService studentService;

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody @Valid Student student) {
        Student savedStudent = studentService.createStudent(student);
        URI location = linkTo(methodOn(StudentController.class)
                .getStudentById(savedStudent.getIdentifier()))
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<?> getStudentsByPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<?> studentsPage = studentService.getStudentsByPagination(page, size);
        return ResponseEntity.ok(studentsPage);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentId) {
        return ResponseEntity.ok().body(studentService.getStudentById(studentId));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudentById(@PathVariable String studentId,
                                                     @Valid @RequestBody StudentUpdateDto dto) {
        Student student = studentService.updateStudentById(studentId, dto);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudentById(@PathVariable String studentId) {
        studentService.deleteById(studentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{studentId}/transactions")
    public ResponseEntity<?> getTransactionByStudentId(@PathVariable String studentId) {
        Student student = studentService.getStudentById(studentId);
        Set<Transaction> transactions = student.getTransactions();
        return ResponseEntity.ok(transactions);
    }
}
