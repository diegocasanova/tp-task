package com.casanova.tp.task.domain.exception;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class OverdraftLimitExceededException extends RuntimeException {
    private static final String MSG = "Overdraft exceeded!! when trying to withdraw [{0}]. " +
        "Current Balance [{1}] with Overdraft Limit [{2}].";

    public OverdraftLimitExceededException(final BigDecimal withdraw,
                                           final BigDecimal currentBalance, final BigDecimal overdraftLimit) {
        super(MessageFormat.format(MSG, String.format("%.2f",withdraw), String.format("%.2f",currentBalance),
            String.format("%.2f",overdraftLimit)));
    }
}
