package com.casanova.tp.task;

import com.casanova.tp.task.domain.Account;
import com.casanova.tp.task.domain.transaction.DepositTransaction;
import com.casanova.tp.task.domain.transaction.PayInterestTransaction;
import com.casanova.tp.task.domain.transaction.TransferTransaction;
import com.casanova.tp.task.domain.transaction.WithdrawTransaction;
import com.casanova.tp.task.service.InMemoryTransactionProcessor;
import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.casanova.tp.task.domain.AccountTestFixture.checkingAccount;
import static com.casanova.tp.task.domain.AccountTestFixture.savingAccount;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionProcessorITTest {

    private InMemoryTransactionProcessor processor;

    @Before
    public void setup() {
        this.processor = new InMemoryTransactionProcessor(accountsMap());
    }

    @Test
    public void shouldProcessSeveralTransactions() {
        processor.addTransaction(new TransferTransaction("1", "1", "2", BigDecimal.valueOf(200)));
        processor.addTransaction(new DepositTransaction("2", "2", BigDecimal.valueOf(300)));
        processor.addTransaction(new DepositTransaction("3", "2", BigDecimal.valueOf(500.90)));
        processor.addTransaction(new WithdrawTransaction("4", "2", BigDecimal.valueOf(200.40)));
        processor.addTransaction(new PayInterestTransaction("5", "5"));
        processor.execute();
        assertThat(processor.getAccounts().get("1").getBalance()).isEqualTo(BigDecimal.valueOf(800.50));
        assertThat(processor.getAccounts().get("2").getBalance()).isEqualTo(BigDecimal.valueOf(1300.50));
        assertThat(processor.getAccounts().get("5").getBalance()).isEqualTo(BigDecimal.valueOf(1268.4862));
    }

    @Test
    public void shouldStoreErrorsIfAccountIsNotFound() {
        processor.addTransaction(new TransferTransaction("1", "1", "2", BigDecimal.valueOf(200)));
        processor.addTransaction(new DepositTransaction("2", "1232", BigDecimal.valueOf(300)));
        processor.addTransaction(new PayInterestTransaction("5", "5"));
        processor.execute();
        assertThat(processor.getAccounts().get("1").getBalance()).isEqualTo(BigDecimal.valueOf(800.50));
        assertThat(processor.getErrors()).hasSize(1);
        assertThat(processor.getErrors().poll().getTransactionId()).isEqualTo("2");
    }

    @Test
    public void shouldTransferCashBetweenTwoAccounts() {
        val transfer = new TransferTransaction("1", "1", "2", BigDecimal.valueOf(600));
        processor.addTransaction(transfer);
        processor.execute();
        assertThat(processor.getAccounts().get("1").getBalance()).isEqualTo(BigDecimal.valueOf(400.50));
        assertThat(processor.getAccounts().get("2").getBalance()).isEqualTo(BigDecimal.valueOf(1100.0));
    }

    @Test
    public void shouldTransferMoneyFromAccountWithAvailableOverdraft() {
        val transfer = new TransferTransaction("1", "3", "4", BigDecimal.valueOf(99.99));
        processor.addTransaction(transfer);
        processor.execute();
        assertThat(processor.getAccounts().get("3").getBalance()).isEqualTo(BigDecimal.valueOf(-99.99));
        assertThat(processor.getAccounts().get("4").getBalance()).isEqualTo(BigDecimal.valueOf(220.89));
    }

    @Test
    public void shouldNotTransferWhenNegativeAmount() {
        val transfer = new TransferTransaction("1", "1", "2", BigDecimal.valueOf(-99.99));
        processor.addTransaction(transfer);
        processor.execute();
        assertThat(processor.getAccounts().get("1").getBalance()).isEqualTo(BigDecimal.valueOf(1000.50));
    }

    @Test
    public void shouldNotTransferWhenAmountZero() {
        val transfer = new TransferTransaction("1", "1", "2", BigDecimal.valueOf(0));
        processor.addTransaction(transfer);
        processor.execute();
        assertThat(processor.getAccounts().get("1").getBalance()).isEqualTo(BigDecimal.valueOf(1000.50));
    }

    @Test
    public void shouldNotTransferWhenAmountGreaterThanBalanceAndOverdraftLimitInSourceAccount() {
        val transfer = new TransferTransaction("1", "1", "2", BigDecimal.valueOf(2000.50));
        processor.addTransaction(transfer);
        processor.execute();
        assertThat(processor.getAccounts().get("1").getBalance()).isEqualTo(BigDecimal.valueOf(1000.50));
    }

    @Test
    public void shouldNotTransferOnTheSameAccount() {
        val transfer = new TransferTransaction("1", "1", "1", BigDecimal.valueOf(100.50));
        processor.addTransaction(transfer);
        processor.execute();
        assertThat(processor.getAccounts().get("1").getBalance()).isEqualTo(BigDecimal.valueOf(1000.50));
    }

    private Map<String, Account> accountsMap() {
        val accounts = new HashMap<String, Account>();
        accounts.put("1", checkingAccount("1", 1000.50));
        accounts.put("2", checkingAccount("2", 500));
        accounts.put("3", checkingAccount("3", 0));
        accounts.put("4", checkingAccount("4", 120.90));
        accounts.put("5", savingAccount("5", 1231.54));
        accounts.put("6", savingAccount("6", 0));
        return accounts;
    }

}
