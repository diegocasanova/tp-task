package com.casanova.tp.task.domain.transaction;

import lombok.Getter;

import java.time.LocalDateTime;



@Getter
public abstract class BaseTransaction implements Transaction {
    protected String transactionId;
    protected String accountNumber;
    protected LocalDateTime when = LocalDateTime.now();

    public BaseTransaction(String transactionId, String accountNumber) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
    }
}
