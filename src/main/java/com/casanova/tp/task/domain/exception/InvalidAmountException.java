package com.casanova.tp.task.domain.exception;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class InvalidAmountException extends RuntimeException {
    private static final String MSG = "Invalid Amount!! Operation: [{0}], amount: [{1}].";

    public InvalidAmountException(final String operation, final BigDecimal amount) {
        super(MessageFormat.format(MSG, operation, String.format("%.2f",amount)));
    }
}
