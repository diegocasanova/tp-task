package com.casanova.tp.task.domain.transaction;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class TransferTransaction extends AmountTransaction {
    private String destinationAccountNumber;

    public TransferTransaction(@NonNull final String transactionId, @NonNull final String sourceAccountNumber,
                               @NonNull final String destinationAccountNumber, @NonNull final BigDecimal amount) {
        super(transactionId, sourceAccountNumber, amount);
        this.destinationAccountNumber = destinationAccountNumber;
    }
}
