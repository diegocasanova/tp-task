package com.casanova.tp.task.domain.transaction;

import com.casanova.tp.task.domain.Account;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class WithdrawTransaction extends BaseTransaction {

    public WithdrawTransaction(@NonNull final String transactionId,@NonNull final String accountNumber,
                               @NonNull final BigDecimal amount) {
        super(transactionId, accountNumber, amount);
    }

    @Override
    public Account execute(final Account account) {
        return account.process(this);
    }
}
