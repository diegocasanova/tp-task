package com.casanova.tp.task.domain.exception;

import java.text.MessageFormat;

public class TransferOnSameAccountException extends RuntimeException {

    private static final String MSG = "Could not do the cash transfer in the same account. Account [{0}].";

    public TransferOnSameAccountException(final String account) {
        super(MessageFormat.format(MSG, account));
    }
}
