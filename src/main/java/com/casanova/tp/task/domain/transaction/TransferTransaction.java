package com.casanova.tp.task.domain.transaction;

import com.casanova.tp.task.domain.CheckingAccount;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

@Value
public class TransferTransaction extends BaseTransaction<CheckingAccount> {

    private String destinationAccountNumber;

    public TransferTransaction(@NonNull final String transactionId, @NonNull final String sourceAccountNumber,
                               @NonNull final String destinationAccountNumber, @NonNull final BigDecimal amount) {
        super(transactionId, sourceAccountNumber, amount);
        this.destinationAccountNumber = destinationAccountNumber;
    }

    @Override
    public CheckingAccount execute(final CheckingAccount account) {
        return null;
    }

    @Override
    public String getAccountNumber() {
        return null;
    }

    public Queue<Transaction<CheckingAccount>> getChildTransactions() {
        Queue<Transaction<CheckingAccount>> queue = new LinkedList<>();
        if (!StringUtils.equals(destinationAccountNumber, accountNumber)) { //same account
            //for test purposes only, we added a transaction numbering for the childs transactions
            queue.add(new WithdrawTransaction(transactionId + ".1", accountNumber, amount));
            queue.add(new DepositTransaction(transactionId + ".2", destinationAccountNumber, amount));
        }
        return queue;
    }

}


