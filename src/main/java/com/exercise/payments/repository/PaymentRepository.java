package com.exercise.payments.repository;

import com.exercise.payments.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findByAmountAndCurrency(
            BigDecimal amount,
            String currency,
            Pageable pageable
    );

    Page<Payment> findByCurrency(String currency, Pageable pageable);

    Page<Payment> findByAmount(BigDecimal amount, Pageable pageable);

}
