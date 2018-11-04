package com.casanova.tp.task.domain.transaction;

import lombok.NonNull;
import lombok.Value;


@Value
public class PayInterestTransaction extends BaseTransaction {
    public PayInterestTransaction(@NonNull final String transactionId, @NonNull final String accountNumber) {
        super(transactionId, accountNumber);
    }
}
