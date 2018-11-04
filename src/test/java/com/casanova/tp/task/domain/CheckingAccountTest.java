package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.InvalidAmountException;
import com.casanova.tp.task.domain.exception.OverdraftLimitExceededException;
import lombok.val;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckingAccountTest {

    public static final double INITIAL_TEST_BALANCE = 200.20;
    public static final int TEST_OVERDRAFT_LIMIT = 100;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void shouldSumToBalanceWhenDeposit() {
        val account = checkingAccount();
        account.deposit(BigDecimal.valueOf(50.70));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(250.90));
    }

    @Test
    public void shouldSumToBalanceWhenBalanceIsNegative() {
        val account = checkingAccount(-99);
        account.deposit(BigDecimal.valueOf(50.70));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(-48.30));
    }

    @Test
    public void shouldSubtractFromBalanceWhenWithdrawWithAmountSmallerThanBalance() {
        val account = checkingAccount();
        account.withdraw(BigDecimal.valueOf(50.70));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(149.50));
    }

    @Test
    public void shouldSubtractFromBalanceWhenWithdrawWithAmountSmallerThanOverdraftLimitAndCurrentBalance() {
        val account = checkingAccount();
        account.withdraw(BigDecimal.valueOf(280));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(-79.80));
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithAmountGreaterThanBalanceAndOverdraftLimit() {
        expectedEx.expect(OverdraftLimitExceededException.class);
        expectedEx.expectMessage("Overdraft exceeded!! when trying to withdraw [350.20]. " +
            "Current Balance [200.20] with Overdraft Limit [100.00].");
        val account = checkingAccount();
        account.withdraw(BigDecimal.valueOf(INITIAL_TEST_BALANCE + TEST_OVERDRAFT_LIMIT + 50));
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithZeroAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [WITHDRAW], amount: [0.00].");
        val account = checkingAccount();
        account.withdraw(BigDecimal.ZERO);
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithNegativaAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [WITHDRAW], amount: [-100.00].");
        val account = checkingAccount();
        account.withdraw(BigDecimal.valueOf(-100));
    }

    @Test
    public void shouldThrowExceptionWhenDepositWithZeroAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [DEPOSIT], amount: [0.00].");
        val account = checkingAccount();
        account.deposit(BigDecimal.ZERO);
    }

    @Test
    public void shouldThrowExceptionWhenDepositWithNegativaAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [DEPOSIT], amount: [-100.00].");
        val account = checkingAccount();
        account.deposit(BigDecimal.valueOf(-100));
    }


    private CheckingAccount checkingAccount() {
        return new CheckingAccount("123471", BigDecimal.valueOf(INITIAL_TEST_BALANCE),
            BigDecimal.valueOf(TEST_OVERDRAFT_LIMIT));
    }

    private CheckingAccount checkingAccount(final float balance) {
        return new CheckingAccount("123471", BigDecimal.valueOf(balance),
            BigDecimal.valueOf(TEST_OVERDRAFT_LIMIT));
    }
}
