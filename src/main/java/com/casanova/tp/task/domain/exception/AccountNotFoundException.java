package com.casanova.tp.task.domain.exception;

import java.text.MessageFormat;

public class AccountNotFoundException extends RuntimeException {
    private static final String MSG = "Account with Number [{0}] not found!!.";

    public AccountNotFoundException(final String accountNumber) {
        super(MessageFormat.format(MSG, accountNumber));
    }
}
