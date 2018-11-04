package com.casanova.tp.task.domain.transaction;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public abstract class AmountTransaction extends BaseTransaction {
    protected BigDecimal amount;

    public AmountTransaction(String transactionId, String accountNumber, BigDecimal amount) {
        super(transactionId, accountNumber);
        this.amount = amount;
    }
}
