package com.casanova.tp.task.domain.transaction;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class DepositTransaction extends AmountTransaction {
    public DepositTransaction(@NonNull final String transactionId,
                              @NonNull final String accountNumber, @NonNull BigDecimal amount) {
        super(transactionId, accountNumber, amount);
    }
}
