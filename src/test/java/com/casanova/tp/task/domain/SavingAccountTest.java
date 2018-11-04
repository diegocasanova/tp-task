package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.InsufficientFundException;
import com.casanova.tp.task.domain.exception.InvalidAmountException;
import com.casanova.tp.task.domain.transaction.PayInterestTransaction;
import lombok.val;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static com.casanova.tp.task.domain.AccountTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SavingAccountTest {




    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void shouldCreateASavingAccountWithZeroInterestIfNegativeInterestRate() {
        val account = new SavingAccount("test", TEST_INITIAL_BALANCE, -0.3);
        assertThat(account.getInterestRate()).isZero();
    }


    @Test
    public void shouldSumToBalanceWhenDeposit() {
        val account = savingAccount();
        val result = account.process(depositTransaction(50.70));
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(250.90));
    }

    @Test
    public void shouldSubtractFromBalanceWhenWithdrawWithAmountSmallerThanBalance() {
        val account = savingAccount();
        val result = account.process(withdrawTransaction(50.70));
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(149.50));
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithAmountGreaterThanBalance() {
        expectedEx.expect(InsufficientFundException.class);
        expectedEx.expectMessage("Insufficient fund!! Trying to withdraw [300.50] with Balance [200.20].");
        val account = savingAccount();
        account.process(withdrawTransaction(300.50f));
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithZeroAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [WITHDRAW], amount: [0.00].");
        val account = savingAccount();
        account.process(withdrawTransaction(0));
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithNegativaAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [WITHDRAW], amount: [-100.00].");
        val account = savingAccount();
        account.process(withdrawTransaction(-100));
    }

    @Test
    public void shouldThrowExceptionWhenDepositWithZeroAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [DEPOSIT], amount: [0.00].");
        val account = savingAccount();
        account.process(depositTransaction(0));
    }

    @Test
    public void shouldThrowExceptionWhenDepositWithNegativaAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [DEPOSIT], amount: [-100.00].");
        val account = savingAccount();
        account.process(depositTransaction(-100));
    }

    @Test
    public void shouldPaytheCorrectInterest() {
        val account = savingAccount();
        val result = account.process(new PayInterestTransaction("1", TEST_ACCOUNT_OWNER));
        val expectedBalance = BigDecimal.valueOf(206.206);
        assertThat(result.getBalance()).isEqualTo(expectedBalance);
    }

    @Test
    public void shouldNoPayInterestIfInterestRateIsZero() {
        val account = new SavingAccount("test", TEST_INITIAL_BALANCE, 0d);
        val result = account.process(new PayInterestTransaction("1", TEST_ACCOUNT_OWNER));
        assertThat(result.getBalance()).isEqualTo(TEST_INITIAL_BALANCE);
    }




}
