package com.casanova.tp.task.domain.transaction;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class WithdrawTransaction extends AmountTransaction {

    public WithdrawTransaction(@NonNull final String transactionId,@NonNull final String accountNumber,
                               @NonNull final BigDecimal amount) {
        super(transactionId, accountNumber, amount);
    }
}
