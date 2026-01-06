package com.system.payment.student_payment_system.services;

import com.system.payment.student_payment_system.database.models.Student;
import com.system.payment.student_payment_system.database.repository.StudentRepository;
import com.system.payment.student_payment_system.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;  // Use concrete class instead of interface for simplicity

    private Student student;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
    }

    @Test
    void createStudent_ShouldReturnStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.createStudent(student);

        assertEquals("John", result.getFirstName());
    }

    @Test
    void getStudentById_WhenFound_ShouldReturnStudent() {
        when(studentRepository.findById("123")).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById("123");

        assertEquals("John", result.getFirstName());
    }

    @Test
    void getStudentById_WhenNotFound_ShouldThrowException() {
        when(studentRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentById("123"));
    }

    @Test
    void deleteStudent_ShouldCallRepositoryDelete() {
        when(studentRepository.findById("123")).thenReturn(Optional.of(student));

        studentService.deleteById("123");

        verify(studentRepository, times(1)).delete(student);
    }
}
