package com.casanova.tp.task.service;

import com.casanova.tp.task.domain.transaction.Transaction;

public interface TransactionProcessor {

    void addTransaction(Transaction transaction);
    void execute();
}
