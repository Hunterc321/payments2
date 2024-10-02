package com.exercise.payments.model;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class PaymentDTO {
    private BigDecimal amount;
    private String currency;
    private String fromAccount;
    private String toAccount;
    private Timestamp timestamp;
}
