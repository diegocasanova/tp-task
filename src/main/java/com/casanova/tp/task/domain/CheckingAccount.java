package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.OverdraftLimitExceededException;
import com.casanova.tp.task.domain.transaction.DepositTransaction;
import com.casanova.tp.task.domain.transaction.WithdrawTransaction;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Value
public class CheckingAccount extends Account {

    private BigDecimal overdraftLimit;

    public CheckingAccount(final String owner, final BigDecimal balance, final BigDecimal overdraftLimit) {
       this(null, owner, balance, overdraftLimit);
    }

    @Builder(toBuilder = true)
    public CheckingAccount(final String number, String owner, final BigDecimal balance,
                           final BigDecimal overdraftLimit) {
        super(owner, balance);
        if (StringUtils.isNotEmpty(number)) {
            this.number = number;
        }
        this.overdraftLimit = overdraftLimit;
    }

    private BigDecimal deposit(final BigDecimal amount) {
        validateAmount(amount, Account.DEPOSIT);
        return balance.add(amount);
    }


    private BigDecimal withdraw(final BigDecimal amount) {
        validateAmount(amount, Account.WITHDRAW);
        if (amount.compareTo(balance.add(overdraftLimit)) > 0) {
            throw new OverdraftLimitExceededException(amount, balance, overdraftLimit);
        }
        return balance.subtract(amount);
    }

    @Override
    public CheckingAccount process(final DepositTransaction transaction) {
        return this.toBuilder().balance(deposit(transaction.getAmount())).build();
    }

    @Override
    public CheckingAccount process(final WithdrawTransaction transaction) {
        return this.toBuilder().balance(withdraw(transaction.getAmount())).build();
    }
}
