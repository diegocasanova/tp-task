package com.casanova.tp.task.domain.transaction;

import com.casanova.tp.task.domain.SavingAccount;
import lombok.NonNull;
import lombok.Value;


@Value
public class PayInterestTransaction extends BaseTransaction<SavingAccount> {
    public PayInterestTransaction(@NonNull final String transactionId, @NonNull final String accountNumber) {
        super(transactionId, accountNumber, null);
    }


    @Override
    public SavingAccount execute(final SavingAccount account) {
        return account.process(this);
    }
}
