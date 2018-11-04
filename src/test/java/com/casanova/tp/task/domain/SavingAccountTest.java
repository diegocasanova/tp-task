package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.InsufficientFundException;
import com.casanova.tp.task.domain.exception.InvalidAmountException;
import lombok.val;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SavingAccountTest {


    public static final double INITIAL_TEST_BALANCE = 200.20;
    public static final float TEST_INTEREST_RATE = 1.05f;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void shouldSumToBalanceWhenDeposit() {
        val account = savingAccount();
        account.deposit(BigDecimal.valueOf(50.70));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(250.90));
    }

    @Test
    public void shouldSubtractFromBalanceWhenWithdrawWithAmountSmallerThanBalance() {
        val account = savingAccount();
        account.withdraw(BigDecimal.valueOf(50.70));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(149.50));
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithAmountGreaterThanBalance() {
        expectedEx.expect(InsufficientFundException.class);
        expectedEx.expectMessage("Insufficient fund!! Trying to withdraw [300.50] with Balance [200.20].");
        val account = savingAccount();
        account.withdraw(BigDecimal.valueOf(300.50));
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithZeroAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [WITHDRAW], amount: [0.00].");
        val account = savingAccount();
        account.withdraw(BigDecimal.ZERO);
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithNegativaAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [WITHDRAW], amount: [-100.00].");
        val account = savingAccount();
        account.withdraw(BigDecimal.valueOf(-100));
    }

    @Test
    public void shouldThrowExceptionWhenDepositWithZeroAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [DEPOSIT], amount: [0.00].");
        val account = savingAccount();
        account.deposit(BigDecimal.ZERO);
    }

    @Test
    public void shouldThrowExceptionWhenDepositWithNegativaAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [DEPOSIT], amount: [-100.00].");
        val account = savingAccount();
        account.deposit(BigDecimal.valueOf(-100));
    }

    private SavingAccount savingAccount() {
        return new SavingAccount("123471", BigDecimal.valueOf(INITIAL_TEST_BALANCE), TEST_INTEREST_RATE);
    }
}
