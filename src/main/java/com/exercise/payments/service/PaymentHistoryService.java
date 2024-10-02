package com.exercise.payments.service;

import com.exercise.payments.model.PaymentHistory;
import com.exercise.payments.repository.PaymentHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class PaymentHistoryService {
    private final PaymentHistoryRepository paymentHistoryRepository;

    public PaymentHistoryService(PaymentHistoryRepository paymentHistoryRepository) {
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    public void logTransactionHistory(Long paymentId, String action) {
        PaymentHistory history = new PaymentHistory();
        history.setPaymentId(paymentId);
        history.setAction(action);
        history.setTimestamp(new Timestamp(System.currentTimeMillis()));
        paymentHistoryRepository.save(history);
    }
}
