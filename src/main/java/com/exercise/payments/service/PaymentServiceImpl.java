package com.exercise.payments.service;

import com.exercise.payments.exception.InvalidInputException;
import com.exercise.payments.exception.PaymentNotFoundException;
import com.exercise.payments.exception.PaymentServiceException;
import com.exercise.payments.model.Payment;
import com.exercise.payments.model.PaymentDTO;
import com.exercise.payments.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService{

    public static final String CREATE = "CREATE";
    public static final String DELETE = "DELETE";

    private final PaymentRepository paymentRepository;
    private final PaymentHistoryService paymentHistoryService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentHistoryService paymentHistoryService) {
        this.paymentRepository = paymentRepository;
        this.paymentHistoryService = paymentHistoryService;
    }

    public Page<PaymentDTO> getPaymentsWithFilters(BigDecimal amount, String currency, Pageable pageable) {
        Page<Payment> payments = findPayments(amount, currency, pageable);

        return payments.map(this::mapToPaymentDTO);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        if (paymentDTO.getAmount() == null || paymentDTO.getCurrency() == null) {
            throw new InvalidInputException("Amount and currency must not be null");
        }
        Payment savedPayment = paymentRepository.save(mapToPayment(paymentDTO));

        paymentHistoryService.logTransactionHistory(savedPayment.getId(), CREATE);

        return mapToPaymentDTO(savedPayment);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean deletePaymentById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            paymentRepository.deleteById(id);
            paymentHistoryService.logTransactionHistory(id, DELETE);
            return true;
        } else {
            throw new PaymentNotFoundException(id);
        }
    }

    private Page<Payment> findPayments(BigDecimal amount, String currency, Pageable pageable) {
        if (Objects.nonNull(amount) && Objects.nonNull(currency)) {
            return paymentRepository.findByAmountAndCurrency(amount, currency, pageable);
        } else if (Objects.nonNull(currency)) {
            return paymentRepository.findByCurrency(currency, pageable);
        } else if (Objects.nonNull(amount)) {
            return paymentRepository.findByAmount(amount, pageable);
        } else {
            return paymentRepository.findAll(pageable);
        }
    }

    private PaymentDTO mapToPaymentDTO(Payment payment) {
        if (Objects.isNull(payment)) {
            throw new PaymentServiceException("Payment entity is null", new NullPointerException());
        }

        PaymentDTO paymentDTO = new PaymentDTO();

        Optional.ofNullable(payment.getAmount())
                .ifPresent(paymentDTO::setAmount);
        Optional.ofNullable(payment.getCurrency())
                .ifPresent(paymentDTO::setCurrency);
        Optional.ofNullable(payment.getFromAccount())
                .ifPresent(paymentDTO::setFromAccount);
        Optional.ofNullable(payment.getToAccount())
                .ifPresent(paymentDTO::setToAccount);
        Optional.ofNullable(payment.getTimestamp())
                .ifPresent(paymentDTO::setTimestamp);

        return paymentDTO;
    }

    private static Payment mapToPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        Optional.ofNullable(paymentDTO.getAmount())
                .ifPresent(payment::setAmount);
        Optional.ofNullable(paymentDTO.getCurrency())
                .ifPresent(payment::setCurrency);
        Optional.ofNullable(paymentDTO.getFromAccount())
                .ifPresent(payment::setFromAccount);
        Optional.ofNullable(paymentDTO.getToAccount())
                .ifPresent(payment::setToAccount);
        payment.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return payment;
    }
}
