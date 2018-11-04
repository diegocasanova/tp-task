package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.InsufficientFundException;
import com.casanova.tp.task.domain.transaction.DepositTransaction;
import com.casanova.tp.task.domain.transaction.PayInterestTransaction;
import com.casanova.tp.task.domain.transaction.WithdrawTransaction;
import lombok.Builder;
import lombok.Value;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Value
public class SavingAccount extends Account {

    private Double interestRate;

    public SavingAccount(String owner, BigDecimal balance, Double interestRate) {
        this(null, owner, balance, interestRate);
    }

    @Builder(toBuilder = true)
    public SavingAccount(String number, String owner, BigDecimal balance, Double interestRate) {
        super(owner, balance);
        if (StringUtils.isNotEmpty(number)) {
            this.number = number;
        }
        if (interestRate < 0) { //if the interest rate in the creation is a negative number we give a zero value
            this.interestRate = 0d;
        } else {
            this.interestRate = interestRate;
        }
    }

    private BigDecimal deposit(final BigDecimal amount) {
        validateAmount(amount, Account.DEPOSIT);
        return balance.add(amount);
    }

    private BigDecimal withdraw(final BigDecimal amount) {
        validateAmount(amount, Account.WITHDRAW);
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundException(amount, balance);
        }
        return balance.subtract(amount);
    }

    public SavingAccount process(final DepositTransaction transaction) {
        return this.toBuilder().balance(deposit(transaction.getAmount())).build();
    }

    public SavingAccount process(final WithdrawTransaction transaction) {
        return this.toBuilder().balance(withdraw(transaction.getAmount())).build();
    }

    public SavingAccount process(final PayInterestTransaction transaction) {
        //some validation to assert if the interest should be paid
        //we always pay interest for test purposes if the interesRate is > 0
        if (interestRate == 0) {
            return this;
        }
        val toPay = balance.multiply(BigDecimal.valueOf(interestRate));
        return this.toBuilder().balance(deposit(toPay)).build();
    }

}
