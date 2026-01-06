package com.system.payment.student_payment_system;

import com.system.payment.student_payment_system.database.models.User;
import com.system.payment.student_payment_system.database.repository.UserRepository;
import com.system.payment.student_payment_system.services.CustomizedUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class StudentPaymentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentPaymentSystemApplication.class, args);
	}



}
