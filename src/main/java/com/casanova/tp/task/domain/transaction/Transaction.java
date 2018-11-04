package com.casanova.tp.task.domain.transaction;

import com.casanova.tp.task.domain.Account;

import java.util.Queue;

public interface Transaction<T extends Account> {
    T execute(T account);
    String getAccountNumber();
    Queue<Transaction<T>> getChildTransactions(); //for composed transactions like Transfers
    String getTransactionId();
}
