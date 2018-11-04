package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.InvalidAmountException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
public abstract class Account {
    public static final String WITHDRAW = "WITHDRAW";
    public static final String DEPOSIT = "DEPOSIT";

    protected String owner;
    protected BigDecimal balance;
    //random account number for test implementation
    protected String number = UUID.randomUUID().toString();

    public Account(final String owner) {
        this.balance = BigDecimal.ZERO;
        this.owner = owner;
    }

    public Account(final String owner, final BigDecimal balance) {
        this.balance = balance;
        this.owner = owner;
    }

    public abstract BigDecimal deposit(final BigDecimal amount);

    public abstract BigDecimal withdraw(BigDecimal amount);

    protected void validateAmount(final BigDecimal amount, final String operation) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new InvalidAmountException(operation, amount);
        }
    }

}
