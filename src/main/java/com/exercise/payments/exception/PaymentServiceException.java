package com.exercise.payments.exception;

public class PaymentServiceException extends RuntimeException{
    public PaymentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
