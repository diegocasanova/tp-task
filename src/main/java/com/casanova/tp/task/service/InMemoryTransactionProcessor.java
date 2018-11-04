package com.casanova.tp.task.service;

import com.casanova.tp.task.domain.Account;
import com.casanova.tp.task.domain.exception.AccountNotFoundException;
import com.casanova.tp.task.domain.transaction.Transaction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * In memory test implementation. It has a queue of transactions to process and a
 * Map of accounts is maintained as an account repository. A queue of errors stores the transactions that
 * couldn't be processed. In a real implementation we should persist the
 * transactions. We can even implement an event sourcing architecture.
 */
@Slf4j
@Getter
public class InMemoryTransactionProcessor implements TransactionProcessor {

    private Map<String, Account> accounts;

    // Transactions Queue
    private Queue<Transaction> transactions = new LinkedList<>();

    // Transactions with errors
    private Queue<Transaction> errors = new LinkedList<>();

    public InMemoryTransactionProcessor(final Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * We save the transactions results in the accounts map in case the transactions are successful
     */
    @Override
    public void execute() {
        for (Transaction transaction : transactions) {
            val childTransactions = transaction.getChildTransactions();
            if (!CollectionUtils.isEmpty(childTransactions)) {
                try {
                    List<Account> results = executeChild(childTransactions);
                    results.forEach(r -> accounts.put(r.getNumber(), r));
                } catch (Exception ex) {
                    storeError(transaction, ex);
                }
            } else {
                try {
                    val result = executeTransaction(transaction);
                    accounts.put(result.getNumber(), result);
                } catch (Exception ex) {
                    storeError(transaction, ex);
                }
            }
        }
    }

    private List<Account> executeChild(final Collection<Transaction> transactions) {
        if (CollectionUtils.isEmpty(transactions)) {
            return Collections.emptyList();
        }
        val results = new ArrayList<Account>();
        for (Transaction transaction : transactions) {
            results.add(executeTransaction(transaction));
        }
        return results;
    }

    private Account executeTransaction(final Transaction transaction) {
        val account = accounts.get(transaction.getAccountNumber());
        if (null == account) {
            throw new AccountNotFoundException(transaction.getAccountNumber());
        }
        return transaction.execute(account);
    }

    private void storeError(Transaction transaction, Exception ex) {
        log.error("Processing transaction id: {}", transaction.getTransactionId(), ex);
        errors.add(transaction);
    }


}
