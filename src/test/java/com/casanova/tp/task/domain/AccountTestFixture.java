package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.transaction.DepositTransaction;
import com.casanova.tp.task.domain.transaction.WithdrawTransaction;

import java.math.BigDecimal;

public class AccountTestFixture {

    public static final int TEST_OVERDRAFT_LIMIT = 100;

    public static final BigDecimal TEST_INITIAL_BALANCE = BigDecimal.valueOf(200.20);
    public static final double TEST_INTEREST_RATE = 0.03;
    public static final String TEST_ACCOUNT_OWNER = "A471812R";
    public static final String TEST_ACCOUNT_NUMBER = "123471";
    public static final String TRANSACTION_ID = "1";

    public static DepositTransaction depositTransaction(final double amount) {
        return new DepositTransaction(TRANSACTION_ID, TEST_ACCOUNT_NUMBER, BigDecimal.valueOf(amount));
    }

    public static  WithdrawTransaction withdrawTransaction(final double amount) {
        return new WithdrawTransaction("1", TEST_ACCOUNT_NUMBER, BigDecimal.valueOf(amount));
    }

    public static  WithdrawTransaction withdrawTransaction(final BigDecimal amount) {
        return new WithdrawTransaction("1", TEST_ACCOUNT_NUMBER, amount);
    }

    public static SavingAccount savingAccount() {
        return new SavingAccount(TEST_ACCOUNT_OWNER, TEST_INITIAL_BALANCE, TEST_INTEREST_RATE);
    }

    public static CheckingAccount checkingAccount() {
        return new CheckingAccount(TEST_ACCOUNT_OWNER, TEST_INITIAL_BALANCE,
            BigDecimal.valueOf(TEST_OVERDRAFT_LIMIT));
    }

    public static CheckingAccount checkingAccount(final double balance) {
        return new CheckingAccount(TEST_ACCOUNT_OWNER, BigDecimal.valueOf(balance),
            BigDecimal.valueOf(TEST_OVERDRAFT_LIMIT));
    }

    public static CheckingAccount checkingAccount(final String number, final double balance) {
        return new CheckingAccount(number,TEST_ACCOUNT_OWNER, BigDecimal.valueOf(balance),
            BigDecimal.valueOf(TEST_OVERDRAFT_LIMIT));
    }
}
