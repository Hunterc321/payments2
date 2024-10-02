package com.exercise.payments.service;

import com.exercise.payments.model.PaymentHistory;
import com.exercise.payments.repository.PaymentHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class PaymentHistoryService {
    private final PaymentHistoryRepository paymentHistoryRepository;

    public void logTransactionHistory(Long paymentId, String action) {
        PaymentHistory history = PaymentHistory.builder()
                .paymentId(paymentId)
                .action(action)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        paymentHistoryRepository.save(history);
    }
}
