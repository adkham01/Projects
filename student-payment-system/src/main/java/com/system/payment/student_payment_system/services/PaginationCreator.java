package com.system.payment.student_payment_system.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;


@Getter
@Setter
public class PaginationCreator {
    private int pageNo = 0;
    private int pageSize = 20;
    private LocalDate from = LocalDate.now().minusDays(7);
    private LocalDate to = LocalDate.now();
    private double minAmount = 0;

    public Pageable getPageable(Integer page, Integer size) {
        Optional.ofNullable(page).filter(p -> p >= 0).ifPresent(p -> this.pageNo = p);
        Optional.ofNullable(size).filter(s -> s > 0).ifPresent(s -> this.pageSize = s);
        return PageRequest.of(pageNo, pageSize);
    }
}