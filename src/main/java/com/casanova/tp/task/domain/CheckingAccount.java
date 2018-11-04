package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.OverdraftLimitExceededException;
import com.casanova.tp.task.domain.exception.TransferOnSameAccountException;
import com.casanova.tp.task.domain.transaction.DepositTransaction;
import com.casanova.tp.task.domain.transaction.TransferTransaction;
import com.casanova.tp.task.domain.transaction.WithdrawTransaction;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Value
public class CheckingAccount extends Account {


    public static final String TRANSFER = "TRANSFER";
    private BigDecimal overdraftLimit;

    public CheckingAccount(String owner, BigDecimal balance,  BigDecimal overdraftLimit) {
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


    private BigDecimal withdraw(BigDecimal amount) {
        validateAmount(amount, Account.WITHDRAW);
        if (amount.compareTo(balance.add(overdraftLimit)) > 0) {
            throw new OverdraftLimitExceededException(amount, balance, overdraftLimit);
        }
        return balance.subtract(amount);
    }

    public CheckingAccount process(final DepositTransaction transaction) {
        return this.toBuilder().balance(deposit(transaction.getAmount())).build();
    }

    public CheckingAccount process(final WithdrawTransaction transaction) {
        return this.toBuilder().balance(withdraw(transaction.getAmount())).build();
    }

    public CheckingAccount process(final TransferTransaction transaction) {
        validateAmount(transaction.getAmount(), TRANSFER);
        if (StringUtils.equals(transaction.getDestinationAccountNumber(), transaction.getAccountNumber())) {
            throw new TransferOnSameAccountException(transaction.getDestinationAccountNumber());
        }
        if (this.getNumber().equals(transaction.getAccountNumber())) { //transfer source
            return this.toBuilder().balance(withdraw(transaction.getAmount())).build();
        } else if (this.getNumber().equals(transaction.getDestinationAccountNumber())) { //transfer destination
            return this.toBuilder().balance(deposit(transaction.getAmount())).build();
        }
        return this; //no transfers
    }
}
