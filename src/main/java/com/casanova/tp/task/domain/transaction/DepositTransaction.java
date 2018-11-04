package com.casanova.tp.task.domain.transaction;

import com.casanova.tp.task.domain.Account;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class DepositTransaction extends BaseTransaction {
    public DepositTransaction(@NonNull final String transactionId,
                              @NonNull final String accountNumber, @NonNull BigDecimal amount) {
        super(transactionId, accountNumber, amount);
    }

    @Override
    public Account execute(final Account account) {
        return account.process(this);
    }
}
