package com.casanova.tp.task.domain.transaction;

import com.casanova.tp.task.domain.Account;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Queue;


@Getter
public abstract class BaseTransaction<T extends Account> implements Transaction<T> {
    protected String transactionId;
    protected String accountNumber;
    protected LocalDateTime when = LocalDateTime.now();
    protected BigDecimal amount;

    public BaseTransaction(final String transactionId, final String accountNumber, final BigDecimal amount) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public Queue<Transaction<T>> getChildTransactions(){
        return null;
    }
}
