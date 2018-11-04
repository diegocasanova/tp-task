package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.InvalidAmountException;
import com.casanova.tp.task.domain.exception.OverdraftLimitExceededException;
import lombok.val;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static com.casanova.tp.task.domain.AccountTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckingAccountTest {


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void shouldSumToBalanceWhenDeposit() {
        val account = checkingAccount();
        val result = account.process(depositTransaction(50.70));
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(250.90));
    }

    @Test
    public void shouldSumToBalanceWhenBalanceIsNegative() {
        val account = checkingAccount(-99);
        val result = account.process(depositTransaction(50.70));
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(-48.30));
    }

    @Test
    public void shouldSubtractFromBalanceWhenWithdrawWithAmountSmallerThanBalance() {
        val account = checkingAccount();
        val result = account.process(withdrawTransaction(50.70));
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(149.50));
    }

    @Test
    public void shouldSubtractFromBalanceWhenWithdrawWithAmountSmallerThanOverdraftLimitAndCurrentBalance() {
        val account = checkingAccount();
        val result = account.process(withdrawTransaction(280));
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(-79.80));
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithAmountGreaterThanBalanceAndOverdraftLimit() {
        expectedEx.expect(OverdraftLimitExceededException.class);
        expectedEx.expectMessage("Overdraft exceeded!! when trying to withdraw [350.20]. " +
            "Current Balance [200.20] with Overdraft Limit [100.00].");
        val account = checkingAccount();
        account.process(withdrawTransaction(TEST_INITIAL_BALANCE
            .add(BigDecimal.valueOf(TEST_OVERDRAFT_LIMIT + 50))));
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithZeroAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [WITHDRAW], amount: [0.00].");
        val account = checkingAccount();
        account.process(withdrawTransaction(BigDecimal.ZERO));
    }

    @Test
    public void shouldThrowExceptionWhenWithdrawWithNegativeAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [WITHDRAW], amount: [-100.00].");
        val account = checkingAccount();
        account.process(withdrawTransaction(-100));
    }

    @Test
    public void shouldThrowExceptionWhenDepositWithZeroAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [DEPOSIT], amount: [0.00].");
        val account = checkingAccount();
        account.process(depositTransaction(0));
    }

    @Test
    public void shouldThrowExceptionWhenDepositWithNegativeAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [DEPOSIT], amount: [-100.00].");
        val account = checkingAccount();
        account.process(depositTransaction(-100));
    }
}
