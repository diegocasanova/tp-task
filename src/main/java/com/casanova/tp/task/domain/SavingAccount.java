package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.InsufficientFundException;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class SavingAccount extends Account {

    private Float interestRate;

    @Builder(toBuilder = true)
    public SavingAccount(String owner, BigDecimal balance, Float interestRate) {
        super(owner, balance);
        this.interestRate = interestRate;
    }

    @Override
    public BigDecimal deposit(final BigDecimal amount) {
        validateAmount(amount, Account.DEPOSIT);
        balance = balance.add(amount);
        return balance;
    }

    @Override
    public BigDecimal withdraw(final BigDecimal amount) {
        validateAmount(amount, Account.WITHDRAW);
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundException(amount, balance);
        }
        balance = balance.subtract(amount);
        return balance;
    }

}
