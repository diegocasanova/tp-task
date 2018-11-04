package com.casanova.tp.task.domain;

import com.casanova.tp.task.domain.exception.InvalidAmountException;
import com.casanova.tp.task.domain.exception.OverdraftLimitExceededException;
import com.casanova.tp.task.domain.exception.TransferOnSameAccountException;
import com.casanova.tp.task.domain.transaction.TransferTransaction;
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

    @Test
    public void shouldTransferMoneyBetweenTwoAccounts() {
        val account1 = checkingAccount("1", 1000.50);
        val account2 = checkingAccount("2", 500);
        val transfer = new TransferTransaction("1", "1", "2", BigDecimal.valueOf(600));
        val res1 = account1.process(transfer);
        val res2 = account2.process(transfer);
        assertThat(res1.getBalance()).isEqualTo(BigDecimal.valueOf(400.50));
        assertThat(res2.getBalance()).isEqualTo(BigDecimal.valueOf(1100.0));
    }

    @Test
    public void shouldTransferMoneyFromAccountWithAvailableOverdraft() {
        val account1 = checkingAccount("1", 0);
        val account2 = checkingAccount("2", 120.90);
        val transfer = new TransferTransaction("1", "1", "2", BigDecimal.valueOf(99.99));
        val res1 = account1.process(transfer);
        val res2 = account2.process(transfer);
        assertThat(res1.getBalance()).isEqualTo(BigDecimal.valueOf(-99.99));
        assertThat(res2.getBalance()).isEqualTo(BigDecimal.valueOf(220.89));
    }

    @Test
    public void shouldThrowExceptionWhenTransferWithNegativeAmount() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [TRANSFER], amount: [-99.99].");
        val account1 = checkingAccount("1", 0);
        val transfer = new TransferTransaction("1", "1", "2", BigDecimal.valueOf(-99.99));
        account1.process(transfer);
    }

    @Test
    public void shouldThrowExceptionWhenTransferWithAmountZero() {
        expectedEx.expect(InvalidAmountException.class);
        expectedEx.expectMessage("Invalid Amount!! Operation: [TRANSFER], amount: [0.00].");
        val account1 = checkingAccount("1", 0);
        val transfer = new TransferTransaction("1", "1", "2", BigDecimal.valueOf(0));
        account1.process(transfer);
    }

    @Test
    public void shouldThrowExceptionWhenTransferWithAmountGreaterThanBalanceAndOverdraftLimitInSourceAccount() {
        expectedEx.expect(OverdraftLimitExceededException.class);
        expectedEx.expectMessage("Overdraft exceeded!! when trying to withdraw [100.50]. " +
            "Current Balance [-50.00] with Overdraft Limit [100.00].");
        val account1 = checkingAccount("1", -50);
        val transfer = new TransferTransaction("1", "1", "2", BigDecimal.valueOf(100.50));
        account1.process(transfer);
    }

    @Test
    public void shouldThrowExceptionWhenTransferOnTheSameAccount() {
        expectedEx.expect(TransferOnSameAccountException.class);
        expectedEx.expectMessage("Could not do the cash transfer in the same account. Account [1].");
        val account1 = checkingAccount("1", -50);
        val transfer = new TransferTransaction("1", "1", "1", BigDecimal.valueOf(100.50));
        account1.process(transfer);
    }


}
