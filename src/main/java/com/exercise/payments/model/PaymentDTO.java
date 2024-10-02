package com.exercise.payments.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class PaymentDTO {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private String currency;
    @NotBlank(message = "From account is required")
    private String fromAccount;
    @NotBlank(message = "To account is required")
    private String toAccount;
    private Timestamp timestamp;
}
