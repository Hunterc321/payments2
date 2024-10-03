package com.exercise.payments.service;

import com.exercise.payments.model.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface PaymentService {
    Page<PaymentDTO> getPaymentsWithFilters(BigDecimal amount, String currency, Pageable pageable);
    PaymentDTO createPayment(PaymentDTO paymentDTO);
    boolean deletePaymentById(Long id);
}
