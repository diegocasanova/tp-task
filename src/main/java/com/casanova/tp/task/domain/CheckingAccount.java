package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.OverdraftLimitExceededException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
public class CheckingAccount extends Account {



    private BigDecimal overdraftLimit;

    @Builder(toBuilder = true)
    public CheckingAccount(String owner, BigDecimal balance,  BigDecimal overdraftLimit) {
        super(owner, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public BigDecimal deposit(final BigDecimal amount) {
        validateAmount(amount, Account.DEPOSIT);
        balance = balance.add(amount);
        return balance;
    }

    @Override
    public BigDecimal withdraw(BigDecimal amount) {
        validateAmount(amount, Account.WITHDRAW);
        if (amount.compareTo(balance.add(overdraftLimit)) > 0) {
            throw new OverdraftLimitExceededException(amount, balance, overdraftLimit);
        }
        balance = balance.subtract(amount);
        return balance;
    }
}
